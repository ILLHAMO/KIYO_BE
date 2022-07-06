package project.kiyobackend.store.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import project.kiyobackend.store.adapter.infrastructure.AWSS3UploadService;
import project.kiyobackend.store.adapter.presentation.dto.StoreRequestDto;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.menu.MenuOption;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;
    private final AWSS3UploadService uploadService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public Slice<Store> getStore(Long lastStoreId, StoreSearchCond storeSearchCond, Pageable pageable)
    {
       return storeQueryRepository.searchBySlice(lastStoreId, storeSearchCond, pageable);
    }

    @Transactional
    public Long saveStore(List<MultipartFile> multipartFiles, StoreRequestDto storeRequestDto)
    {
        System.out.println(multipartFiles.get(0).getOriginalFilename());
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

        List<Menu> result = storeRequestDto.getMenus().stream().map(m ->
                new Menu(m.getName()
                        , m.getMenuOptions().stream().map(mo -> new MenuOption(mo.getName()))
                        .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());

        Store store = Store.createStore(storeRequestDto.getName(), storeRequestDto.getCall(), storeRequestDto.getComment(), storeRequestDto.getTime(), storeRequestDto.isKids(), storeRequestDto.getCategoryIds(), storeRequestDto.getConvenienceIds(), result, fileNameList);
        storeRepository.save(store);
        return store.getId();
    }

    private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }
    
}
