package project.kiyobackend.store.adapter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.store.adapter.presentation.dto.*;
import project.kiyobackend.store.application.BookmarkService;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RestController
@Tag(name = "store controller",description = "Store API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController{

    private final StoreService storeService;
    private final BookmarkService bookmarkService;

    @Operation(summary = "가게 목록 페이징 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "successful operation")
    })
    @GetMapping("/stores")
    public Slice<StoreResponse> getStoreBySlice(@CurrentUser User currentUser , @RequestParam(name = "lastStoreId", required = false)  Long lastStoreId, Pageable pageable, StoreSearchCond storeSearchCond)
    {
        Slice<Store> search = storeService.getStore(currentUser,lastStoreId,storeSearchCond,pageable);
        return StoreAssembler.storeResponseDto(search);
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<StoreDetailResponse> getDetailStoreInfo(@CurrentUser User currentUser, @PathVariable Long id)
    {
        Store findStore = storeService.getStoreById(currentUser, id);
        StoreDetailResponse result = StoreDetailResponse.builder().name(findStore.getName()).kids(findStore.isKids())
                .isBooked(findStore.isBooked())
                .simpleComment(findStore.getComment().getSimpleComment())
                .tags(findStore.getTagStores().stream().map(ts -> ts.getTag()).collect(Collectors.toList()))
                .address(findStore.getAddress())
                .time(findStore.getTime())
                .detailComment(findStore.getComment().getDetailComment())
                .addressMap("address_map")
                .images(findStore.getStoreImages())
                .convenienceIds(findStore.getConvenienceIds())
                .menus(findStore.getMenus())
                .reviews(findStore.getReviews())
                .build();
        return ResponseEntity.ok(result);

    }

    @PostMapping(value = "/store")
    public Long saveStore(
            @RequestPart(name = "meta_data") StoreRequestDto storeRequestDto,
            @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles )
    {
        return storeService.saveStore(multipartFiles,storeRequestDto);
    }



    @PutMapping("/store/{id}/bookmark")
    public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long id, @CurrentUser User user)
    {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.addBookmark(user, id);
        BookmarkResponse bookmarkResponse = StoreAssembler.bookmarkResponse(bookmarkResponseDto);
        return ResponseEntity.ok(bookmarkResponse);
    }

    @DeleteMapping("/store/{id}/bookmark")
    public ResponseEntity<BookmarkResponse> removeBookmark(@PathVariable Long id,@CurrentUser User user)
    {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.removeBookmark(user, id);
        BookmarkResponse bookmarkResponse = StoreAssembler.bookmarkResponse(bookmarkResponseDto);
        return ResponseEntity.ok(bookmarkResponse);
    }

}
