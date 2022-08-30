package project.kiyobackend.admin.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.admin.store.presentation.dto.StoreQueryDto;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.dto.StorePaginationDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreAdminService {

    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;

    public Page<StorePaginationDto> getStore(StoreQueryDto storeQueryDto, Pageable pageable){
        return storeQueryRepository.searchByPage(pageable, storeQueryDto.isAssigned(), storeQueryDto.getSearch());
    }

    @Transactional
    public boolean assignStore(Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        store.assignStore();
        return store.isAssigned();
    }
}
