package project.kiyobackend.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.store.adapter.presentation.dto.StoreRequestDto;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.menu.MenuOption;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;

    public Slice<Store> getStore(Long lastStoreId, StoreSearchCond storeSearchCond, Pageable pageable)
    {
       return storeQueryRepository.searchBySlice(lastStoreId, storeSearchCond, pageable);
    }


    @Transactional
    public Long saveStore(StoreRequestDto storeRequestDto)
    {
        List<Menu> result = storeRequestDto.getMenus().stream().map(m ->
                new Menu(m.getName()
                        , m.getMenuOptions().stream().map(mo -> new MenuOption(mo.getName()))
                        .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
        Store store = Store.createStore(storeRequestDto.getName(), storeRequestDto.getCall(), storeRequestDto.getComment(), storeRequestDto.getTime(), storeRequestDto.isKids(), storeRequestDto.getCategoryIds(), storeRequestDto.getConvenienceIds(), result, storeRequestDto.getImages());
        storeRepository.save(store);
        return store.getId();
    }











}
