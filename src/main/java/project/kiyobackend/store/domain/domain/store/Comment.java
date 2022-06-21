package project.kiyobackend.store.domain.domain.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Comment {

    @Column(name = "simple_comment")
    private String simpleComment;

    @Column(name = "detail_comment")
    private String detailComment;
}
