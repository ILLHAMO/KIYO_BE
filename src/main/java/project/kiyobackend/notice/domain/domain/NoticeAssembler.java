package project.kiyobackend.notice.domain.domain;

import org.springframework.data.domain.Slice;
import project.kiyobackend.notice.presentation.dto.NoticeResponse;

public class NoticeAssembler {

    public static Slice<NoticeResponse> NoticeResponse(Slice<Notice> notices)
    {
        return notices.map(n-> new NoticeResponse(n.getId(),n.getTitle(),n.getContent()));
    }

}
