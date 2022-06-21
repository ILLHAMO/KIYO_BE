package project.kiyobackend.store.domain.domain.category;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
public class Category {

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