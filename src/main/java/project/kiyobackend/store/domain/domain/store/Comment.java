package project.kiyobackend.store.domain.domain.store;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class Comment {

    @Column(name = "simple_comment")
    private String simpleComment;

    @Column(name = "detail_comment")
    private String detailComment;
}
