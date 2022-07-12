package project.kiyobackend.review.domain.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "review_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewImage extends JpaBaseEntity {

    @Id
    @Column(name = "review_image_id")
    @GeneratedValue
    private Long id;

    @Column(name = "image_path")
    private String path;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "upload_time")
//    private Date uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewImage(String path)
    {
        this.path = path;
    }
}
