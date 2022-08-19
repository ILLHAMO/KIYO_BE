package project.kiyobackend.review.domain.domain;

import lombok.*;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreImage;
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

    private void setUser(User user)
    {
        this.user = user;
        user.getReviews().add(this);
    }

    private void setStore(Store store)
    {
        this.store = store;
        store.getReviews().add(this);
    }



    private void setReviewImages(List<String> reviewImagesPath)
    {
        for (String path : reviewImagesPath) {
            ReviewImage reviewImage = new ReviewImage(path);
            this.reviewImages.add(reviewImage);
            reviewImage.setReview(this);

        }
    }

    @Builder
    public Review(Long id, String content, Score score, User user, Store store) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.user = user;
        this.store = store;
    }

    public static Review createReview(User user, Store store,Score score,String content, List<String> fileNameList)
    {
        Review review = Review.builder().score(score).content(content).build();
        review.setUser(user);
        review.setStore(store);
        review.setReviewImages(fileNameList);
        return review;
    }

    public void updateReview(List<String> reviewImagePath, Score score,String content)
    {

        if(score != null)
        {
            this.score = score;
        }
        if(content != null)
        {
            this.content = content;
        }
        if(reviewImagePath != null)
        {
            setReviewImages(reviewImagePath);

        }
    }
}
