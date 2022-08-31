package project.kiyobackend.admin.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.admin.store.presentation.dto.StoreDetailResponseForUpdate;
import project.kiyobackend.admin.store.presentation.dto.StoreQueryDto;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.presentation.dto.StoreAssembler;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.dto.StorePaginationDto;
import project.kiyobackend.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreAdminService {

    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;

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
}
