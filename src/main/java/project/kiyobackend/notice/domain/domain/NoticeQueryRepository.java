package project.kiyobackend.notice.domain.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static project.kiyobackend.notice.domain.domain.QNotice.notice;
import static project.kiyobackend.store.domain.domain.store.QStore.store;


@Repository
public class NoticeQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;


    public NoticeQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public Slice<Notice> getNotice(Long lastStoreId,Pageable pageable)
    {
        List<Notice> results = query.selectFrom(notice)
                .where(ltNoticeId(lastStoreId))
                .orderBy(notice.id.desc())
                .limit(pageable.getPageSize()+1) // 나는 5개 요청해도 쿼리상 +시켜서 6개 들고 오게 함
                .fetch();

        return checkLastPage(pageable, results);
    }

    private BooleanExpression ltNoticeId(Long storeId) {
        if (storeId == null) {
            return null;
        }

        return store.id.lt(storeId);
    }

    private Slice<Notice> checkLastPage(Pageable pageable, List<Notice> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
