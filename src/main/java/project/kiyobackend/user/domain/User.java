package project.kiyobackend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.kiyobackend.QnA.domain.QnA;
import project.kiyobackend.auth.entity.SnsType;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends JpaBaseEntity {

    /*
    엔티티이기 때문에 기본적인 id값 설정
     */
    @JsonIgnore
    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    /*
    실제 user 구분에 사용되는 값, OAuth2User의 id값을 사용한다.
     */
    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "username", length = 100)
    private String username;

    private String nickname;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<QnA> qnAs = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<BookMark> bookMarks = new ArrayList<>();


    // password는 따로 필요 없다.
    @JsonIgnore
    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "email", length = 512, unique = true)
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    private String emailVerifiedYn;

    @Column(name = "PROFILE_IMAGE_URL", length = 512)
    private String profileImageUrl;

    @Column(name = "SNS_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public User(
              String userId,
             String username,
             String email,
             String emailVerifiedYn,
             String profileImageUrl,
             SnsType snsType,
             RoleType roleType
    ) {
        this.userId = userId;
        this.username = username;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.snsType = snsType;
        this.roleType = roleType;
    }
}