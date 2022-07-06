package project.kiyobackend.bookmark.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.bookmark.application.BookmarkService;
import project.kiyobackend.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark/{storeId}")
    public ResponseEntity<String> addLike(@CurrentUser User user, @PathVariable(name = "storeId") Long storeId)
    {
        boolean result = false;
        result = bookmarkService.addLike(user, storeId);

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/bookmark/{storeId}")
    public ResponseEntity<String> removeLike(@CurrentUser User user, @PathVariable(name = "storeId") Long storeId)
    {
        boolean result = false;
        result = bookmarkService.removeLike(user, storeId);

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
