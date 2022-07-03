package project.kiyobackend.store.application;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.bookmark.domain.BookmarkRepository;
import project.kiyobackend.store.query.StoreQueryRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class StoreService {

    private final StoreQueryRepository storeQueryRepository;

    private final BookmarkRepository bookmarkRepository;









}
