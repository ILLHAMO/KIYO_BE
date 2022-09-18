package project.kiyobackend.store.domain.domain.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends JpaBaseEntity {

    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_category")
    private String name;

    @OneToMany(mappedBy = "menu",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MenuOption> menuOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void setMenuOptions(List<MenuOption> menuOptions)
    {
        if(menuOptions != null)
        {
            for (MenuOption menuOption : menuOptions) {
                this.menuOptions.add(menuOption);
                menuOption.setMenu(this);
            }
        }

    }

    public Menu(String name,List<MenuOption> menuOptions)
    {
        this.name = name;
        setMenuOptions(menuOptions);
    }
}
