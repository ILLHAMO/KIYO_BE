package project.kiyobackend.category.domain;

import lombok.Getter;
import org.yaml.snakeyaml.events.Event;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
public class Category extends JpaBaseEntity {

    // 그냥 id로 참조해봐야겠다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;


    protected Category() {
    }
    public Category(String name) {
        this.name = name;
    }

}