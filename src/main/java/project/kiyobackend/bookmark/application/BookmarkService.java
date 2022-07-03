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

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BookmarkRepository bookmarkRepository;

    public boolean addLike(String userId, Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException());
        User user = userRepository.findByUserId(userId);

        // 만약 아직 등록되지 않았다면 추가 가능하다
        if(isNotRegisteredLike(store,user))
        {
             bookmarkRepository.save(new BookMark(user,store));
             return true;
        }
        return false;

    }

    public boolean removeLike(String userId, Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException());
        User user = userRepository.findByUserId(userId);

        Optional<BookMark> bookMark = bookmarkRepository.findByUserAndStore(user, store);
        if(bookMark.isPresent())
        {
            bookmarkRepository.delete(bookMark.get());
            return true;
        }
        return false;

    }


    // 아직 북마크 등록이 안된게 맞는지 체크하는 로직
    private boolean isNotRegisteredLike(Store store, User user)
    {
        return bookmarkRepository.findByUserAndStore(user,store).isEmpty();
    }

    private boolean isRegisteredLike(Store store, User user)
    {
        return bookmarkRepository.findByUserAndStore(user,store).isPresent();
    }
}
