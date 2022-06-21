package project.kiyobackend.store.domain.domain.convenience;

import lombok.Getter;
import project.kiyobackend.store.domain.domain.category.CategoryId;

import javax.persistence.*;

@Entity
@Table(name = "convenience")
@Getter
public class Convenience {

    @EmbeddedId
    private ConvenienceId id;

    @Column(name = "convenience_name")
    private String name;
}
