package project.kiyobackend.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookMark,Long> {
}
