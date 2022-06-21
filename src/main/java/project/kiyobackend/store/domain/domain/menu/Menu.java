package project.kiyobackend.store.domain.domain.menu;

import org.yaml.snakeyaml.events.Event;
import project.kiyobackend.store.domain.domain.store.Store;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {

    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_category")
    private String name;

    @OneToMany(mappedBy = "menu")
    private List<MenuOption> menuOptions;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
