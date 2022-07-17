package project.kiyobackend.review.domain.domain;

import lombok.*;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends JpaBaseEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private Score score;

    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Review(Long id, String content, Score score, List<ReviewImage> reviewImages, User user, Store store) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.reviewImages = reviewImages;
        this.user = user;
        this.store = store;
    }
}
