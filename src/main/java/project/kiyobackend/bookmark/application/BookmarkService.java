package project.kiyobackend.bookmark.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.bookmark.domain.BookMark;
import project.kiyobackend.bookmark.domain.BookmarkRepository;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final StoreRepository storeRepository;
    private final BookmarkRepository bookmarkRepository;

    public boolean addLike(User user, Long storeId)
    {
        // 상점중에서 해당 id로 조회
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException());

        Optional<BookMark> bookMark = bookmarkRepository.findByUserAndStore(user, store);

        if(bookMark.isEmpty())
        {
             bookmarkRepository.save(new BookMark(user,store));
             store.addBookmarkCount();
             return true;
        }
        return false;

    }

    public boolean removeLike(User user, Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException());
        Optional<BookMark> bookMark = bookmarkRepository.findByUserAndStore(user, store);
        if(bookMark.isPresent())
        {
            bookmarkRepository.delete(bookMark.get());
            store.minusBookmarkCount();
            return true;
        }
        return false;
    }
}
