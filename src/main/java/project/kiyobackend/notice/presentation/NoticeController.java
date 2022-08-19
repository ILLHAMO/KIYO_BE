package project.kiyobackend.notice.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.notice.application.NoticeService;
import project.kiyobackend.notice.domain.domain.Notice;
import project.kiyobackend.notice.domain.domain.NoticeAssembler;
import project.kiyobackend.notice.presentation.dto.NoticeResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "전체 공지사항 조회")
    @GetMapping
    public ResponseEntity<Slice<NoticeResponse>> getNotice(@RequestParam(name = "lastStoreId", required = false)  Long lastStoreId, Pageable pageable)
    {
        Slice<Notice> results = noticeService.getNotice(lastStoreId, pageable);
        return ResponseEntity.ok(NoticeAssembler.NoticeResponse(results));

    }

}
