package project.kiyobackend.store.adapter.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.bookmark.domain.BookMark;
import project.kiyobackend.store.adapter.presentation.dto.StoreRequestDto;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.menu.MenuOption;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreResponseDto;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController{

    // 간단한 CRUD 전용
    private final StoreRepository storeRepository;
    // 복잡한 쿼리 전용
    private final StoreQueryRepository storeQueryRepository;

    private final UserRepository userRepository;


    @GetMapping("/stores")
    public Slice<StoreResponseDto> getStoreBySlice(@CurrentUser User user , @RequestParam(name = "lastStoreId") Long lastStoreId, Pageable pageable, StoreSearchCond storeSearchCond)
    {

        List<BookMark> bookMarks = user.getBookMarks();

        Slice<Store> search = storeQueryRepository.searchBySlice(lastStoreId, storeSearchCond, pageable);

        for(BookMark bookMark : bookMarks)
        {
            Long storeId = bookMark.getStore().getId();
            Optional<Store> storeOpt = search.getContent().stream().filter(
                    store -> Objects.equals(store.getId(), storeId)
            ).findFirst();
            storeOpt.ifPresent(store -> store.setIsBooked(true));
        }

        return search.map(s -> new StoreResponseDto(s.getId(),
                s.isKids(),
                s.getStoreImages(),
                s.getName(),
                s.getReviewCount(),
                s.getBookmarkCount(),
                s.isBooked()));
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
    public String test(@CurrentUser User user)
    {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getUsername());
        System.out.println(user.getUserId());
        return "login success";
    }

    private User getUser(UserDetails userPrincipal)
    {
        return userRepository.findByUserId(userPrincipal.getUsername());
    }
}
