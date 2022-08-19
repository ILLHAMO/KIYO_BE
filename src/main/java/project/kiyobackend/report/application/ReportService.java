package project.kiyobackend.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.report.domain.service.ReportDomainService;
import project.kiyobackend.user.domain.User;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {


    private final ReportDomainService reportDomainService;

    @Transactional
    public Long saveReport(User currentUser, Long reviewId,String content) {
        return reportDomainService.saveReport(currentUser,reviewId,content);
    }
}
