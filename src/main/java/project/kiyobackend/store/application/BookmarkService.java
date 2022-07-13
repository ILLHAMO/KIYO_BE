package project.kiyobackend.store.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public BookmarkResponseDto addBookmark(User user, Long storeId)
    {
        // 상점중에서 해당 id로 조회
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        System.out.println(store);
        User user2 = userRepository.findByUserId(user.getUserId()).get();
        store.addBookmark(user2);

        return new BookmarkResponseDto(store.getBookmarkCounts(), true);

    }

    public BookmarkResponseDto removeBookmark(User user, Long storeId)
    {
        Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
        User user2 = userRepository.findByUserId(user.getUserId()).get();
        store.removeBookmark(user2);
        return new BookmarkResponseDto(store.getBookmarkCounts(),false);

    }
}
