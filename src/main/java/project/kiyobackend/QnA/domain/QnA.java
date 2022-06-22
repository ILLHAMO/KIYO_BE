package project.kiyobackend.QnA.domain;

import lombok.Getter;
import project.kiyobackend.user.domain.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String answer;

    private LocalDate answerCreatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
