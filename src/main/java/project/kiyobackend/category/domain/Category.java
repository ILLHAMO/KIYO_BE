package project.kiyobackend.category.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends JpaBaseEntity {

    // 그냥 id로 참조해봐야겠다.
    @Id
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

}