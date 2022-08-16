package project.kiyobackend.QnA.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;



@Repository
public class QnAQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public QnAQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Slice<Qna> searchBySlice(String currentUserId, Long lastStoreId, Pageable pageable)
    {
        List<Qna> results = query.selectFrom(QQna.qna)
                .where(

                        ltStoreId(lastStoreId)
                )
                .orderBy(QQna.qna.id.desc())
                .limit(pageable.getPageSize()+1) // 나는 5개 요청해도 쿼리상 +시켜서 6개 들고 오게 함
                .fetch();

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if(results.size() > pageable.getPageSize())
        {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results,pageable,hasNext);
    }
    private BooleanExpression ltStoreId(Long qnaId) {
        if (qnaId == null) {
            return null;
        }

        return QQna.qna.id.lt(qnaId);
    }
}
