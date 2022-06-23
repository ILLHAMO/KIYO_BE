package project.kiyobackend.category.domain;

import lombok.Getter;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
public class Category extends JpaBaseEntity {

    @EmbeddedId
    private CategoryId id;

    @Column(name = "category_name")
    private String name;

    protected Category() {
    }
    public Category(CategoryId id, String name) {
        this.id = id;
        this.name = name;
    }

}