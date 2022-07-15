package project.kiyobackend.store.domain.domain.tag;

import lombok.Getter;
import project.kiyobackend.store.domain.domain.store.Store;

import javax.persistence.*;

@Entity
@Getter
public class TagStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
