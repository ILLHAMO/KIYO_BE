package project.kiyobackend.store.adapter.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.store.adapter.presentation.dto.BookmarkResponse;
import project.kiyobackend.store.adapter.presentation.dto.StoreAssembler;
import project.kiyobackend.store.adapter.presentation.dto.StoreResponse;
import project.kiyobackend.store.application.BookmarkService;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.store.adapter.presentation.dto.StoreRequestDto;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.domain.domain.store.Store;
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
    private final BookmarkService bookmarkService;

    //TODO : 무조건 사용자 있을때만 로직 수행되도록 변경 필요
    @GetMapping("/stores")
    public Slice<StoreResponse> getStoreBySlice(@CurrentUser User currentUser , @RequestParam(name = "lastStoreId", required = false)  Long lastStoreId, Pageable pageable, StoreSearchCond storeSearchCond)
    {
        Slice<Store> search = storeService.getStore(currentUser,lastStoreId,storeSearchCond,pageable);
        return StoreAssembler.storeResponseDto(search);
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
