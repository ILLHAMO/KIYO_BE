package project.kiyobackend.store.domain.domain.tag;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    // TODO : 동작 시켜보면서 로직 체크
    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL)
    private List<TagStore> tagStores = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }
}
