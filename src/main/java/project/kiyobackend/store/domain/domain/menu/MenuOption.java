package project.kiyobackend.store.domain.domain.menu;

import lombok.Getter;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Getter
public class MenuOption extends JpaBaseEntity {

    @Id
    @Column(name = "menu_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_detail")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
