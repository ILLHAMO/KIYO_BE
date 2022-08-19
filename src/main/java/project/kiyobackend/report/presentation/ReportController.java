package project.kiyobackend.report.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.common.SuccessResponseDto;
import project.kiyobackend.report.application.ReportService;
import project.kiyobackend.report.presentation.dto.ReportRequestDto;
import project.kiyobackend.review.application.ReviewService;
import project.kiyobackend.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "부적절한 리뷰에 대해서 신고하기")
    @PostMapping("/review/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SuccessResponseDto> saveReport(@CurrentUser User currentUser, @PathVariable Long reviewId, @RequestBody ReportRequestDto reportRequestDto)
    {
        Long result = reportService.saveReport(currentUser, reviewId, reportRequestDto.getContent());
        return ResponseEntity.ok(new SuccessResponseDto(true,result));

    }



}
