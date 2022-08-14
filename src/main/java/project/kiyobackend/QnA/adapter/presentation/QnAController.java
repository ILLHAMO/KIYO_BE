package project.kiyobackend.QnA.adapter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.QnA.adapter.presentation.dto.QnASaveRequest;
import project.kiyobackend.QnA.application.QnAService;
import project.kiyobackend.QnA.application.dto.QnAResponseDto;
import project.kiyobackend.QnA.application.dto.QnaRequestDto;
import project.kiyobackend.QnA.domain.QnA;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.common.SuccessResponseDto;
import project.kiyobackend.user.domain.User;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnAService;

    @Operation(summary = "문의 사항 리스트 조회")
    @GetMapping
    public ResponseEntity<Slice<QnAResponseDto>> getQnAList(@CurrentUser User user, @RequestParam(name = "lastStoreId",required = false) Long lastStoreId, Pageable pageable)
    {
        Slice<QnA> qnAList = qnAService.getQnAList(user, lastStoreId, pageable);
        Slice<QnAResponseDto> result = qnAList.map(q -> new QnAResponseDto(q.getContent()));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "사용자가 문의사항 추가")
    @PostMapping
    public ResponseEntity<SuccessResponseDto> addQnA(@CurrentUser User user, @RequestBody QnASaveRequest qnASaveRequest)
    {
        Long qnaId = qnAService.saveQnA(user, new QnaRequestDto(qnASaveRequest.getContent()));
        return ResponseEntity.ok(new SuccessResponseDto(true,qnaId));
    }

    @Operation(summary = "사용자가 문의 사항 삭제")
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<String> removeQnA(@PathVariable Long qnaId)
    {
        String result = qnAService.removeQnA(qnaId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "사용자가 문의 사항 수정")
    @PutMapping("/{qnaId}")
    public void updateQnA()
    {

    }
}
