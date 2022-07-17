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
import project.kiyobackend.store.adapter.presentation.dto.store.StoreDetailResponse;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreRequestDto;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreResponse;
import project.kiyobackend.store.application.BookmarkService;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.application.UserService;
import project.kiyobackend.user.domain.User;
import java.util.List;


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


    // TODO : 상세 페이지에서 본인의 리뷰는 삭제 및 수정 가능
    // TODO : 자주 변경되는 것이므로, 엔티티에 필드로 넣기 보다는 DTO 만들때 넣어주자, 북마크 여부도 똑같이 수정
    @Operation(summary = "상세 페이지 조회")
    @GetMapping("/store/{id}")
    public ResponseEntity<StoreDetailResponse> getDetailStoreInfo(@CurrentUser User currentUser, @PathVariable Long id)
    {
        StoreDetailResponseDto storeDetailResponseDto = storeService.getStoreById(currentUser, id);
        StoreDetailResponse result = StoreAssembler.storeDetailResponse(storeDetailResponseDto);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "가게 등록")
    @PostMapping(value = "/store")
    public Long saveStore(
            @RequestPart(name = "meta_data") StoreRequestDto storeRequestDto,
            @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles )
    {
        return storeService.saveStore(multipartFiles,storeRequestDto);
    }

    @Operation(summary = "북마크 추가")
    @PutMapping("/store/{id}/bookmark")
    public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long id, @CurrentUser User user)
    {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.addBookmark(user, id);
        BookmarkResponse bookmarkResponse = StoreAssembler.bookmarkResponse(bookmarkResponseDto);
        return ResponseEntity.ok(bookmarkResponse);
    }


    @Operation(summary = "북마크 삭제")
    @DeleteMapping("/store/{id}/bookmark")
    public ResponseEntity<BookmarkResponse> removeBookmark(@PathVariable Long id,@CurrentUser User user)
    {
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.removeBookmark(user, id);
        BookmarkResponse bookmarkResponse = StoreAssembler.bookmarkResponse(bookmarkResponseDto);
        return ResponseEntity.ok(bookmarkResponse);
    }

}
