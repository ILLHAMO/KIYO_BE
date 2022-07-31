package project.kiyobackend.QnA.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QnASaveRequest {

    private String title;
    private String content;
}
