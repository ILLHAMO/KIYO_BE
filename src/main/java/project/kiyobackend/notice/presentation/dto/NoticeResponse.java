package project.kiyobackend.notice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeResponse {

    private Long noticeId;
    private String title;
    private String content;

}
