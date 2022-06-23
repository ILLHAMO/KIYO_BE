package project.kiyobackend.convenience.domain;

import lombok.Getter;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "convenience")
@Getter
public class Convenience extends JpaBaseEntity {

    @EmbeddedId
    private ConvenienceId id;

    @Column(name = "convenience_name")
    private String name;
}
