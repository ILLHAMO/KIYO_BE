package project.kiyobackend.bookmark.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void setStore(Store store)
    {
        this.store = store;
        store.getBookMarks().add(this);
    }

    public BookMark(User user, Store store) {
        this.setStore(store);
        this.setUser(user);
    }

    public void setUser(User user)
    {
        this.user = user;
        user.getBookMarks().add(this);
    }



}
