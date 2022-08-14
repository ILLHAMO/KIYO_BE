package project.kiyobackend.QnA.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnA extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    //private String title;

    private String content;

    // 만약 사용자의 문의 사항이 삭제 되면, 관리자가 남긴 문의 사항도 자동 삭제 되도록 만듦
    @OneToOne(mappedBy = "qna",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QnAReply qnAReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public QnA(String content, User user)
    {
        this.content = content;
        this.user = user;
    }
}
