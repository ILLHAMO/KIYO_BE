package project.kiyobackend.report.domain.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.user.domain.User;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private String content;

    @OneToOne
    private User user;

    @OneToOne
    private Review review;

    public static Report createReport(String content,User user, Review review)
    {
       return new Report(content,user,review);
    }

    private Report(String content,User user, Review review)
    {
        this.content = content;
        this.user = user;
        this.review = review;
    }
}
