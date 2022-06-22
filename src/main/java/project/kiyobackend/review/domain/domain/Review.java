package project.kiyobackend.review.domain.domain;

import lombok.Getter;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private Score score;
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(
//            name = "review_image",
//            joinColumns = @JoinColumn(name = "store_id"))
//    @OrderColumn(name = "image_idx")
    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
