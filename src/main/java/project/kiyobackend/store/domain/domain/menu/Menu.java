package project.kiyobackend.store.domain.domain.menu;

import lombok.Getter;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Menu extends JpaBaseEntity {

    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_category")
    private String name;

    @OneToMany(mappedBy = "menu",fetch = FetchType.LAZY)
    private List<MenuOption> menuOptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
