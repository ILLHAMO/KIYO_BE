package project.kiyobackend.bookmark.domain;

import lombok.Getter;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "bookmark_table")
public class BookMark extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

}
