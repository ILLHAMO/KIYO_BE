package project.kiyobackend.common.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.kiyobackend.QnA.domain.QnA;
import project.kiyobackend.auth.entity.RoleType;
import project.kiyobackend.auth.entity.SnsType;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.user.domain.User;

import java.util.ArrayList;
import java.util.List;


public class MockUser {

    private MockUser()
    {

    }
    public static Builder builder()
    {
        return new Builder();
    }
    public static class Builder {
        private Long userSeq;
        private String userId;
        private String username = "jemin";
        private String email = "jemin3161@naver.com";
        private String nickname = "nickname";
        private List<Review> reviews = new ArrayList<Review>();
        private List<QnA> qnAS = new ArrayList<QnA>();
        private String password = "hanyang";
        private String emailVerifiedYn = "Y";
        private String profileImageUrl = "s3://ap-north-east";
        private SnsType snsType = SnsType.GOOGLE;
        private RoleType roleType = RoleType.USER;



        public Builder userSeq(Long userSeq)
        {
            this.userSeq = userSeq;
            return this;
        }

        public Builder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder username(String username)
        {
            this.username = username;
            return this;
        }

        public Builder password(String password)
        {
            this.password = password;
            return this;
        }

        public Builder emailVerifiedYn(String emailVerifiedYn)
        {
            this.emailVerifiedYn  = emailVerifiedYn;
            return this;
        }

        public Builder profileImageUrl(String profileImageUrl)
        {
            this.profileImageUrl = profileImageUrl;
            return this;
        }

        public User build()
        {
            return new User(userSeq,
                    userId,
                    username,
                    nickname,
                    reviews,
                    qnAS,
                    new ArrayList<BookMark>(),
                    password,
                    email,
                    emailVerifiedYn,
                    profileImageUrl,
                    snsType,
                    roleType);
        }
    }
}
