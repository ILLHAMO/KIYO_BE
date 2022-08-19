package project.kiyobackend.notice.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.notice.domain.domain.Notice;
import project.kiyobackend.notice.domain.domain.NoticeQueryRepository;
import project.kiyobackend.notice.domain.domain.NoticeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeQueryRepository noticeQueryRepository;

    public Slice<Notice> getNotice(Long lastStoreId, Pageable pageable) {

       return noticeQueryRepository.getNotice(lastStoreId,pageable);
    }
}
