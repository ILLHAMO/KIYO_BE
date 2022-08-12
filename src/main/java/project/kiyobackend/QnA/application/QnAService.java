package project.kiyobackend.QnA.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.QnA.application.dto.QnAResponseDto;
import project.kiyobackend.QnA.domain.QnA;
import project.kiyobackend.QnA.domain.QnAQueryRepository;
import project.kiyobackend.QnA.domain.QnARepository;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QnAService {

    private final QnARepository qnARepository;
    private final QnAQueryRepository qnAQueryRepository;

    private Slice<QnA> getQnAList(User currentUser, Long lastStoreId, Pageable pageable)
    {

        Slice<QnA> qna = qnAQueryRepository.searchBySlice(currentUser.getUserId(),lastStoreId,pageable);
        return qna;
    }

    private void saveQnA()
    {

    }

}
