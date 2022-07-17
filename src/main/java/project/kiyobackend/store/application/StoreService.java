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
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.adapter.infrastructure.AWSS3UploadService;
import project.kiyobackend.store.adapter.presentation.dto.StoreAssembler;
import project.kiyobackend.store.adapter.presentation.dto.review.ReviewResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreRequestDto;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.menu.MenuOption;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreQueryRepository storeQueryRepository;

    private final StoreRepository storeRepository;

    private final AWSS3UploadService uploadService;

    private final UserRepository userRepository;

    public Slice<Store> getStore(User currentUser,Long lastStoreId, StoreSearchCond storeSearchCond, Pageable pageable)
    {
        Slice<Store> stores = storeQueryRepository.searchBySlice(lastStoreId, storeSearchCond, pageable);
        Optional<User> findUser = userRepository.findByUserId(currentUser.getUserId());
        if(findUser.isPresent())
        {
            List<BookMark> bookMarks = findUser.get().getBookMarks();
            checkCurrentUserBookmarked(stores,bookMarks);
        }

        return stores;

    }

    public StoreDetailResponseDto getStoreById(User user , Long storeId)
    {
        Store store = storeQueryRepository.getStoreDetail(storeId);
        Optional<User> findUser = userRepository.findByUserId(user.getUserId());
        if(store != null)
        {
            List<BookMark> bookMarks = findUser.get().getBookMarks();
            checkCurrentUserBookmarkedForDetail(store,bookMarks);
        }
        StoreDetailResponseDto storeDetailResponseDto = StoreAssembler.storeDetailResponseDto(store);
        List<ReviewResponseDto> reviewResponses = storeDetailResponseDto.getReviewResponses();
        for (ReviewResponseDto reviewRespons : reviewResponses) {
            if(reviewRespons.getReviewerName().equals(findUser.get().getUserId()))
            {
                reviewRespons.setCurrentUserReview(true);
            }
        }
        return storeDetailResponseDto;
    }

    @Transactional
    public Long saveStore(List<MultipartFile> multipartFiles, StoreRequestDto storeRequestDto)
    {
        List<String> fileNameList = getMultipartFileNames(multipartFiles);

        List<Menu> menus = convertMenuDtoToMenuEntity(storeRequestDto);

        Store store = Store.createStore(storeRequestDto.getName(),
                storeRequestDto.getCall(),
                storeRequestDto.getComment(),
                storeRequestDto.getTime(),storeRequestDto.getAddress(),
                storeRequestDto.isKids(),
                storeRequestDto.getCategoryIds(),
                storeRequestDto.getConvenienceIds(),
                menus,
                fileNameList);

        storeRepository.save(store);

        return store.getId();
    }


    private List<Menu> convertMenuDtoToMenuEntity(StoreRequestDto storeRequestDto) {
        List<Menu> result = storeRequestDto.getMenus().stream().map(m ->
                new Menu(m.getName()
                        , m.getMenuOptions().stream().map(mo -> new MenuOption(mo.getName()))
                        .collect(Collectors.toList())
                )

        ).collect(Collectors.toList());
        return result;
    }


    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {
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
    
}
