package project.kiyobackend.review.adapter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.review.adapter.presentation.dto.ReviewAssembler;
import project.kiyobackend.review.adapter.presentation.dto.ReviewRequest;
import project.kiyobackend.review.adapter.presentation.dto.ReviewResponse;
import project.kiyobackend.review.application.ReviewService;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreRequestDto;
import project.kiyobackend.user.domain.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰")
    @PostMapping(value = "/review/store/{storeId}")
    public ResponseEntity<ReviewResponse> saveReview(
            @RequestPart(name = "meta_data") ReviewRequest reviewRequest,
            @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles,
            @CurrentUser User user,
            @PathVariable Long storeId)
    {
        System.out.println("reviewRequest" + reviewRequest.getContent());
        Long result = reviewService.saveReview(user.getUserId(), storeId, multipartFiles, ReviewAssembler.reviewRequestDto(reviewRequest));
        return ResponseEntity.ok(new ReviewResponse(result));
    }
}
