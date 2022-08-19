package project.kiyobackend.report.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.exception.review.NotExistReviewException;
import project.kiyobackend.exception.user.NotExistUserException;
import project.kiyobackend.report.domain.domain.Report;
import project.kiyobackend.report.domain.domain.ReportRepository;
import project.kiyobackend.review.application.ReviewService;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.review.domain.domain.ReviewRepository;
import project.kiyobackend.user.application.UserService;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportDomainService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReportRepository reportRepository;

    public Long saveReport(User currentUser, Long reviewId,String content) {

        User findUser = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(NotExistReviewException::new);
        Report report = Report.createReport(content, findUser, findReview);
        Report saveReport = reportRepository.save(report);
        return saveReport.getId();

    }
}
