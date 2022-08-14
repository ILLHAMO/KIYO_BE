package project.kiyobackend.QnA.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.kiyobackend.QnA.application.dto.QnaRequestDto;
import project.kiyobackend.QnA.domain.QnA;
import project.kiyobackend.QnA.domain.QnAQueryRepository;
import project.kiyobackend.QnA.domain.QnARepository;
import project.kiyobackend.exception.qna.NotExistQnAException;
import project.kiyobackend.exception.user.NotExistUserException;

import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;



@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class QnAService {

    private final QnARepository qnARepository;
    private final QnAQueryRepository qnAQueryRepository;
    private final UserRepository userRepository;

    public Slice<QnA> getQnAList(User currentUser, Long lastStoreId, Pageable pageable)
    {

        Slice<QnA> qna = qnAQueryRepository.searchBySlice(currentUser.getUserId(),lastStoreId,pageable);
        return qna;
    }

    @Transactional
    public Long saveQnA(User currentUser, QnaRequestDto qnaRequestDto)
    {
        User findUser = userRepository.findByUserId(currentUser.getUserId()).orElseThrow(NotExistUserException::new);
        QnA qna = qnARepository.save(new QnA(qnaRequestDto.getContent(), findUser));
        return qna.getId();
    }

    @Transactional
    public String removeQnA(Long qnaId)
    {
        QnA qnA = qnARepository.findById(qnaId).orElseThrow(NotExistQnAException::new);
        qnARepository.delete(qnA);
        return "success delete";
    }

}
