package project.kiyobackend.store.adapter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.common.SuccessResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.*;
import project.kiyobackend.store.adapter.presentation.dto.store.*;
import project.kiyobackend.store.application.BookmarkService;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.application.dto.UserBookmarkResponseDto;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;
import java.util.List;


@Slf4j
@RestController
@Tag(name = "STORE API",description = "가게 API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController{

    private final StoreService storeService;
    private final BookmarkService bookmarkService;

    @Operation(summary = "가게 목록 페이징 조회")
    @PreAuthorize("hasRole('USER') or isAnonymous()")
    @GetMapping("/stores")
    public ResponseEntity<Slice<StoreResponse>> getStoreBySlice(@CurrentUser User currentUser , @RequestParam(name = "lastStoreId", required = false)  Long lastStoreId, Pageable pageable, StoreSearchCond storeSearchCond)
    {
        Slice<Store> search = storeService.getStore(currentUser,lastStoreId,storeSearchCond,pageable);
        return ResponseEntity.ok(StoreAssembler.storeResponseDto(search));
    }


    // TODO : 상세 페이지에서 본인의 리뷰는 삭제 및 수정 가능
    // TODO : 자주 변경되는 것이므로, 엔티티에 필드로 넣기 보다는 DTO 만들때 넣어주자, 북마크 여부도 똑같이 수정
    @Operation(summary = "상세 페이지 조회")
    @PreAuthorize("hasRole('USER') or isAnonymous()")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<StoreDetailResponse> getDetailStoreInfo(@CurrentUser User currentUser, @PathVariable Long storeId)
    {
        StoreDetailResponseDto storeDetailResponseDto = storeService.getStoreDetail(currentUser, storeId);
        StoreDetailResponse result = StoreAssembler.storeDetailResponse(storeDetailResponseDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "가게 등록")
    @PostMapping(value = "/store")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SuccessResponseDto> saveStore(
            @CurrentUser User user,
            @RequestPart(name = "meta_data") StoreRequestDto storeRequestDto,
            @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles )
    {
        System.out.println("들어옴! ");
        return ResponseEntity.ok(new SuccessResponseDto(true,storeService.saveStore(user,multipartFiles,storeRequestDto)));
    }

    @Operation(summary = "가게 삭제")
    @DeleteMapping("/store/{storeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SuccessResponseDto> removeStore(@PathVariable Long storeId)
    {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok(new SuccessResponseDto(true,storeId));
    }

    @Operation(summary = "현재 사용자가 북마크한 가게 조회")
    @GetMapping("/store/bookmark")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Slice<UserBookmarkResponse>> getBookmarkedStore(@CurrentUser User currentUser, @RequestParam(name = "lastStoreId",required = false) Long lastStoreId,Pageable pageable)
    {
        Slice<UserBookmarkResponseDto> bookmarkedStore = storeService.getBookmarkedStore(currentUser, lastStoreId, pageable);
        return ResponseEntity.ok(StoreAssembler.userBookmarkResponse(bookmarkedStore));
    }

    @Operation(summary = "북마크 추가")
    @PutMapping("/store/{id}/bookmark")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long id, @CurrentUser User user)
    {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.addBookmark(user, id);
        BookmarkResponse bookmarkResponse = StoreAssembler.bookmarkResponse(bookmarkResponseDto);
        return ResponseEntity.ok(bookmarkResponse);
    }


    @Operation(summary = "북마크 삭제")
    @DeleteMapping("/store/{id}/bookmark")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookmarkResponse> removeBookmark(@PathVariable Long id,@CurrentUser User user)
    {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.removeBookmark(user, id);
        BookmarkResponse bookmarkResponse = StoreAssembler.bookmarkResponse(bookmarkResponseDto);
        return ResponseEntity.ok(bookmarkResponse);
    }

    @Operation(summary = "가게 검색")
    @PreAuthorize("isAnonymous() or hasRole('USER')")
    @GetMapping("/store/search")
    public ResponseEntity<Slice<StoreSearchResponse>> searchStore(@CurrentUser User currentUser,@RequestParam(name = "keyword") String keyword,
                                              @RequestParam(name = "lastStoreId", required = false)  Long lastStoreId,
                                              Pageable pageable,
                                              StoreSearchCond storeSearchCond)
    {
        Slice<Store> stores = storeService.searchStoreByKeyword(currentUser, keyword, lastStoreId, storeSearchCond, pageable);
        return ResponseEntity.ok(StoreAssembler.storeSearchResponses(stores));
    }

    @Operation(summary = "키워드 인기 검색 순위")
    @PreAuthorize("isAnonymous() or hasRole('USER')")
    @GetMapping("/search/keyword/rank")
    public ResponseEntity<List<SearchRankingResponseDto>> getPopularKeyword()
    {
        List<SearchRankingResponseDto> ranking = storeService.findKeywordSortedByRank();
        return ResponseEntity.ok(ranking);
    }

}
