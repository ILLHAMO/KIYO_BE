package project.kiyobackend.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookMark,Long> {

    Optional<BookMark> findByUserAndStore(User user, Store store);
}
