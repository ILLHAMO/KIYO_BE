package project.kiyobackend.QnA.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaReply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_reply_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "qna_id")
    private Qna qna;

  //  private String title;

    private String content;
}
