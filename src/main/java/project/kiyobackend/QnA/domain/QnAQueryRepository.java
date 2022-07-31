package project.kiyobackend.QnA.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;

import javax.persistence.EntityManager;
import java.util.List;

import static project.kiyobackend.QnA.domain.QQnA.*;
import static project.kiyobackend.store.domain.domain.store.QStore.store;

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

    public Slice<QnA> searchBySlice(String currentUserId, Long lastStoreId, Pageable pageable)
    {
        List<QnA> results = query.selectFrom(qnA)
                .where(

                        ltStoreId(lastStoreId)
                )
                .orderBy(qnA.id.desc())
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

        return qnA.id.lt(qnaId);
    }
}
