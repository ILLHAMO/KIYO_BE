package project.kiyobackend.store.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.store.adapter.presentation.dto.StoreRequestDto;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.menu.MenuOption;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreResponseDto;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController{

    // 간단한 CRUD 전용
    private final StoreRepository storeRepository;
    // 복잡한 쿼리 전용
    private final StoreQueryRepository storeQueryRepository;


    @GetMapping("/store")
    public Slice<StoreResponseDto> getStoreBySlice(@RequestParam(name = "lastStoreId") Long lastStoreId, Pageable pageable, StoreSearchCond storeSearchCond)
    {

        Slice<Store> search = storeQueryRepository.searchBySlice(lastStoreId, storeSearchCond, pageable);

        Slice<StoreResponseDto> result = search.map(s -> new StoreResponseDto(s.getId(), s.isKids(), s.getStoreImages(), s.getName(), s.getReviewCount(), s.getBookmarkCount()));
        return result;
    }

    @PostMapping("/store")
    public String saveStore(@RequestBody StoreRequestDto storeRequestDto)
    {
        List<Menu> result = storeRequestDto.getMenus().stream().map(m ->
                new Menu(m.getName()
                        , m.getMenuOptions().stream().map(mo -> new MenuOption(mo.getName()))
                        .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
        Store store = Store.createStore(storeRequestDto.getName(), storeRequestDto.getCall(), storeRequestDto.getComment(), storeRequestDto.getTime(), storeRequestDto.isKids(), storeRequestDto.getCategoryIds(), storeRequestDto.getConvenienceIds(), result, storeRequestDto.getImages());
        storeRepository.save(store);
        return "success";
    }



    @GetMapping("/store")
    public String test()
    {
        return "login success";
    }

}
