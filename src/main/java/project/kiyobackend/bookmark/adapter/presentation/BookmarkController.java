package project.kiyobackend.bookmark.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.bookmark.application.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark/{storeId}")
    public ResponseEntity<String> addLike(@PathVariable(name = "storeId") Long storeId)
    {
        boolean result = false;
        result = bookmarkService.addLike("333523646", storeId);

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/bookmark/{storeId}")
    public ResponseEntity<String> removeLike(@PathVariable(name = "storeId") Long storeId)
    {
        boolean result = false;
        result = bookmarkService.removeLike("333523646", storeId);

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
