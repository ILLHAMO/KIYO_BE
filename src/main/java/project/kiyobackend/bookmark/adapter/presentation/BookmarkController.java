package project.kiyobackend.bookmark.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.bookmark.adapter.presentation.dto.BookmarkRequestDto;
import project.kiyobackend.bookmark.application.BookmarkService;
import project.kiyobackend.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<String> addLike(@CurrentUser User user,@RequestBody BookmarkRequestDto bookmarkRequestDto)
    {
        boolean result = false;
        result = bookmarkService.addLike(user, bookmarkRequestDto.getStoreId());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<String> removeLike(@CurrentUser User user,@RequestBody BookmarkRequestDto bookmarkRequestDto)
    {
        boolean result = false;
        result = bookmarkService.removeLike(user, bookmarkRequestDto.getStoreId());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
