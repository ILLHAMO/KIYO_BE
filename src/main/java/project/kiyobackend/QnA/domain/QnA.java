package project.kiyobackend.QnA.domain;

import lombok.Getter;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;

import javax.persistence.*;

@Entity
@Getter
public class QnA extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
