package project.kiyobackend.review.domain.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "review_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewImage {

    @Id
    @Column(name = "image_id")
    @GeneratedValue
    private Long id;

    @Column(name = "image_path")
    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    private Date uploadTime;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewImage(String path)
    {
        this.uploadTime = new Date();
        this.path = path;
    }
}
