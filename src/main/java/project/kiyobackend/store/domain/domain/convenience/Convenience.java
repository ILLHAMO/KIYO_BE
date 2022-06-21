package project.kiyobackend.store.domain.domain.convenience;

import javax.persistence.*;

@Entity
public class Convenience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "convenience_id")
    private Long id;
}
