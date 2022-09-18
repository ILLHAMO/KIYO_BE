package project.kiyobackend.admin.store.application;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import project.kiyobackend.admin.store.presentation.dto.StoreDetailRequestForUpdate;
import project.kiyobackend.admin.store.presentation.dto.StoreDetailResponseForUpdate;
import project.kiyobackend.admin.store.presentation.dto.StoreQueryDto;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.menu.MenuOption;
import project.kiyobackend.store.domain.domain.store.Opentime;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreImage;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.domain.domain.tag.Tag;
import project.kiyobackend.store.infrastructure.AWSS3UploadService;
import project.kiyobackend.store.presentation.dto.StoreAssembler;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.dto.StorePaginationDto;
import project.kiyobackend.user.domain.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreAdminService {

    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;
    private final AWSS3UploadService uploadService;

    public Page<StorePaginationDto> getStore(StoreQueryDto storeQueryDto, Pageable pageable){
        return storeQueryRepository.searchByPage(pageable, storeQueryDto.isAssigned(), storeQueryDto.getSearch());
    }

    public StoreDetailResponseForUpdate getStoreDetailForUpdate(Long storeId)
    {
        Store store = storeQueryRepository.getStoreDetail(storeId);
        if(store == null)
        {
            throw new NotExistStoreException();
        }
        return StoreAssembler.storeDetailResponseForUpdate(store);
    }

    @Transactional
    public boolean assignStore(Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        store.assignStore();
        return store.isAssigned();
    }

    @Transactional
    public Long updateStore(Long storeId, StoreDetailRequestForUpdate storeDetailRequestForUpdate,List<MultipartFile> multipartFiles) {

        // 1. 트랜잭션 내부에서 기존의 가게 가져옴 영속 상태
        Store store1 = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);

        // 2. 파일 이름 빼옴
        List<String> fileNameList = getMultipartFileNames(multipartFiles);

        // 3. 아예 storeImage 다 날려버림, orphanRemoval에 따라서 다 삭제되야 함
        store1.getStoreImages().clear();
        store1.getMenus().clear();

        store1.updateStore(storeDetailRequestForUpdate.getName(),
                storeDetailRequestForUpdate.isKids(),
                storeDetailRequestForUpdate.getSimpleComment(),
                storeDetailRequestForUpdate.getTag().stream().map(t -> new Tag(t.getName())).collect(Collectors.toList()),
                storeDetailRequestForUpdate.getAddress(),
                storeDetailRequestForUpdate.getTime(),
                storeDetailRequestForUpdate.getDetailComment(),
                storeDetailRequestForUpdate.getAddressMap(),
                fileNameList,
                storeDetailRequestForUpdate.getConvenienceIds(),
                storeDetailRequestForUpdate.getCategoryIds()
                );
        store1.setMenus(storeDetailRequestForUpdate.getMenuRequests().stream().map(m -> new Menu(m.getName(),m.getMenuOptions().stream().map(mo -> new MenuOption(mo.getName())).collect(Collectors.toList())
                )).collect(Collectors.toList()));

        return store1.getId();


    }

    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {

        // TODO : 아예 리스트 안보내는거 가정하고 체크
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
}
