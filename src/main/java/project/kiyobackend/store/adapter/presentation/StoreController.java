package project.kiyobackend.store.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.convenience.domain.ConvenienceRepository;
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
public class StoreController{

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;

    /**
     * page로 store 조회
     */
    @GetMapping("/getstorebypage")
    public Page<StoreResponseDto> getStoreByPage( Pageable pageable,StoreSearchCond storeSearchCond)
    {
        Page<Store> search = storeQueryRepository.searchByPage(storeSearchCond, pageable);
        return search.map(s->new StoreResponseDto(s.getId(),s.isKids(),s.getStoreImages(),s.getName(),s.getReviewCount(),s.getBookmarkCount()));
    }

    /**
     * slice로 store 조회
     */
    @GetMapping("/getstorebyslice")
    public Slice<StoreResponseDto> getStoreBySlice(Pageable pageable, StoreSearchCond storeSearchCond)
    {
        Slice<Store> search = storeQueryRepository.searchBySlice(storeSearchCond, pageable);
        Slice<StoreResponseDto> result = search.map(s -> new StoreResponseDto(s.getId(), s.isKids(), s.getStoreImages(), s.getName(), s.getReviewCount(), s.getBookmarkCount()));
        return result;
    }

    /**
     * store 생성
     */
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
}
