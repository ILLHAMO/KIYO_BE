package project.kiyobackend.store.domain.domain.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "store_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StoreImage {

    @Id
    @Column(name = "store_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path")
    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    private Date uploadTime;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public StoreImage(String path)
    {
        this.uploadTime = new Date();
        this.path = path;
    }
}
