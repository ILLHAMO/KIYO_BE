package project.kiyobackend.review.adapter.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.kiyobackend.review.domain.domain.Score;

import java.util.Scanner;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequest {

    private Score score;
    private String content;
}
