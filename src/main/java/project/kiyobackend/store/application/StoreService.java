package project.kiyobackend.store.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.store.infrastructure.AWSS3UploadService;
import project.kiyobackend.store.presentation.dto.SearchRankingResponseDto;
import project.kiyobackend.store.presentation.dto.StoreAssembler;
import project.kiyobackend.store.presentation.dto.review.ReviewResponseDto;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.presentation.dto.store.StoreRequestDto;
import project.kiyobackend.store.application.dto.UserBookmarkResponseDto;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreQueryRepository storeQueryRepository;

    private final StoreRepository storeRepository;

    private final AWSS3UploadService uploadService;

    private final UserRepository userRepository;

    private final RedisSearchService redisSearchService;


    /**
     * 비로그인 유저일때는 북마크 여부 체크 안하고, 로그인 유저일때만 체크함
     */
    public Slice<Store> getStore(User currentUser,Long lastStoreId, StoreSearchCond storeSearchCond, Pageable pageable,String address)
    {
        Slice<Store> stores = storeQueryRepository.searchBySlice(lastStoreId, storeSearchCond, pageable,address);

        // 로그인 상태일때와 아닐때를 분기
        if(currentUser != null)
        {
            // TODO : 애초에 currentUser가 null인지로 검증했고, 실제 DB에 유저가 없다면 내가 설정한 예외로 관리해주는게 맞다고 생각
            User findUser = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);
            checkCurrentUserBookmarked(stores,findUser.getBookMarks());

        }
        return stores;
    }

    public Slice<Store> searchStoreByKeyword(User currentUser, String keyword, Long lastStoreId, StoreSearchCond storeSearchCond, Pageable pageable)
    {

        Slice<Store> stores = storeQueryRepository.searchByKeyword(keyword, lastStoreId, storeSearchCond, pageable);
        if(currentUser != null)
        {
            Optional<User> findUser = userRepository.findByUserId(currentUser.getUserId());
            if(findUser.isPresent())
            {
                List<BookMark> bookMarks = findUser.get().getBookMarks();
                checkCurrentUserBookmarked(stores,bookMarks);
            }
        }
        System.out.println("레디스 호출 진입");
        redisSearchService.addKeywordToRedis(keyword);
        return stores;

    }

    public List<SearchRankingResponseDto> findKeywordSortedByRank()
    {
        return redisSearchService.findKeywordSortedByRank();
    }

    public List<Store> getStoreCurrentUserAssigned(List<Long> assignedList)
    {
        return storeQueryRepository.getStoreCurrentUserAssigned(assignedList);
    }

    public StoreDetailResponseDto getStoreDetail(User user, Long storeId)
    {
        // 특정 가게 정보 찾고
        Store store = storeQueryRepository.getStoreDetail(storeId);
        if(store == null)
        {
            throw new NotExistStoreException();
        }
        // 현재 사용자 로그인 되어 있다면
        if(user != null)
        {
            // 실제 트랜잭션 안에서 user 정보를 가져옴
            User findUser = userRepository.findByUserId(user.getUserId()).orElseThrow(NotExistUserException::new);

            checkCurrentUserBookmarkedForDetail(store,findUser.getBookMarks());
            StoreDetailResponseDto storeDetailResponseDto = StoreAssembler.storeDetailResponseDto(store);
            checkCurrentUserReviewed(findUser, storeDetailResponseDto.getReviewResponses());
            return storeDetailResponseDto;
        }
        // 익명 사용자
        else{
            StoreDetailResponseDto storeDetailResponseDto = StoreAssembler.storeDetailResponseDto(store);
            return storeDetailResponseDto;
        }
    }

    public Slice<UserBookmarkResponseDto> getBookmarkedStore(User currentUser, Long lastStoreId, Pageable pageable)
    {
        User user = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);
        Slice<Store> bookmarkedStore = storeQueryRepository.getBookmarkedStore(user.getUserId(), lastStoreId, pageable);
        return StoreAssembler.userBookmarkResponseDto(bookmarkedStore);

    }

    @Transactional
    public Long saveStore(User currentUser, List<MultipartFile> multipartFiles, StoreRequestDto storeRequestDto)
    {
        User user = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);

        List<String> fileNameList = getMultipartFileNames(multipartFiles);

        Store store = Store.createStoreForUser(storeRequestDto.getName(),
                storeRequestDto.getCall(),
                storeRequestDto.getAddress(),
                storeRequestDto.isKids(),
                storeRequestDto.getCategoryIds(),
                storeRequestDto.getConvenienceIds(),
                fileNameList,user.getUserSeq());

        Store saveStore = storeRepository.save(store);
        user.getAssignedStoreList().add(saveStore.getId());

        return saveStore.getId();
    }

    @Transactional
    public boolean assignStore(Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        store.assignStore();
        return store.isAssigned();
    }

    @Transactional
    public void deleteStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        storeRepository.delete(store);
    }


    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {

        if(multipartFiles != null)
        {
            List<String> fileNameList = new ArrayList<>();

            multipartFiles.forEach(file->{
                String fileName = createFileName(file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try(InputStream inputStream = file.getInputStream()) {
                    uploadService.uploadFile(inputStream,objectMetadata,fileName);
                } catch(IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
                }
                fileNameList.add(uploadService.getFileUrl(fileName));
            });
            return fileNameList;
        }
        return null;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }

    public void checkCurrentUserBookmarked(Slice<Store> search,List<BookMark> bookMarks)
    {
        if(!bookMarks.isEmpty())
        {
            for(BookMark bookMark : bookMarks)
            {
                Long storeId = bookMark.getStore().getId();
                Optional<Store> storeOpt = search.getContent().stream().filter(
                        store -> Objects.equals(store.getId(), storeId)
                ).findFirst();
                storeOpt.ifPresent(store -> store.setIsBooked(true));
            }
        }
    }

    public void checkCurrentUserBookmarkedForDetail(Store store,List<BookMark> bookMarks)
    {
        if(!bookMarks.isEmpty())
        {
            // 해당 유저의 북마크 들 중
            for(BookMark bookMark : bookMarks)
            {
                Long storeId = bookMark.getStore().getId();
                if (store.getId().equals(storeId))
                {
                    store.setIsBooked(true);
                    return;
                }

            }
        }
    }

    private void checkCurrentUserReviewed(User user, List<ReviewResponseDto> reviewResponses) {


            for (ReviewResponseDto reviewRespons : reviewResponses) {

                if(reviewRespons.getReviewerId().equals(user.getUserId()))
                {
                    reviewRespons.setCurrentUserReview(true);
                }
            }


    }



}
