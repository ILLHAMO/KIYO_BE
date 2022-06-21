package project.kiyobackend.store.domain.domain.menu;

import javax.persistence.*;

@Entity
public class MenuOption {

    @Id
    @Column(name = "menu_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_detail")
    private String name;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
