package project.kiyobackend.store.domain.domain.bookmark;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookmark_table")
public class BookMark extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void setStore(Store store)
    {
        this.store = store;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookMark bookMark = (BookMark) o;
        return Objects.equals(store, bookMark.getStore()) &&
                Objects.equals(user, bookMark.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, user);
    }


}
