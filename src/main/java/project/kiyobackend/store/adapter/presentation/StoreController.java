package project.kiyobackend.store.adapter.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.bookmark.domain.BookMark;
import project.kiyobackend.store.adapter.presentation.dto.StoreRequestDto;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.query.StoreResponseDto;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController{

    private final StoreService storeService;

    @GetMapping("/stores")
    public Slice<StoreResponseDto> getStoreBySlice(@CurrentUser User currentUser , @RequestParam(name = "lastStoreId") Long lastStoreId, Pageable pageable, StoreSearchCond storeSearchCond)
    {

        Slice<Store> search = storeService.getStore(lastStoreId,storeSearchCond,pageable);

        List<BookMark> bookMarks = currentUser.getBookMarks();

        checkCurrentUserBookmarked(search,bookMarks);

        return search.map(s -> new StoreResponseDto(s.getId(),
                s.isKids(),
                s.getStoreImages(),
                s.getName(),
                s.getReviewCount(),
                s.getBookmarkCount(),
                s.isBooked()));
    }

    @PostMapping("/store")
    public Long saveStore(@RequestBody StoreRequestDto storeRequestDto)
    {
        return storeService.saveStore(storeRequestDto);
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
}
