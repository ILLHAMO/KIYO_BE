package project.kiyobackend.convenience.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "convenience")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Convenience extends JpaBaseEntity {

    @Id
    @Column(name = "convenience_id")
    private Long id;

    @Column(name = "convenience_name")
    private String name;
}
