package project.kiyobackend.store.domain.domain.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "store_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StoreImage extends JpaBaseEntity {

    @Id
    @Column(name = "store_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path")
    private String path;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "upload_time")
//    private Date uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public StoreImage(String path)
    {
        this.path = path;
    }
}
