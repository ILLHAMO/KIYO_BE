package project.kiyobackend.store.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import project.kiyobackend.store.domain.domain.bookmark.QBookMark;
import project.kiyobackend.store.domain.domain.store.Store;
import javax.persistence.EntityManager;
import java.util.List;

import static project.kiyobackend.store.domain.domain.bookmark.QBookMark.*;
import static project.kiyobackend.store.domain.domain.store.QStore.store;
import static project.kiyobackend.store.domain.domain.store.QStoreImage.*;


@Repository
public class StoreQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public StoreQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 메인 페이지 조회 기능
     */
    public Slice<Store> searchBySlice(Long lastStoreId, StoreSearchCond condition, Pageable pageable)
    {
        List<Store> results = query.selectFrom(store)
                .where(
                        // 관리자가 승인한 가게만 보여야 한다.
               //         store.isAssigned.eq(true),
                        // no-offset 페이징 처리
                        ltStoreId(lastStoreId),
                        // Category 중복 필터링
                        eqConvenience(condition.getConvenienceIds()),
                        eqCategory(condition.getCategoryIds())
                        // Convenience 중복 필터링

                )
                .orderBy(store.id.desc())
                .limit(pageable.getPageSize()+1) // 나는 5개 요청해도 쿼리상 +시켜서 6개 들고 오게 함
                .fetch();

        return checkLastPage(pageable, results);
    }


    /**
     * 가게 검색 기능
     */
    public Slice<Store> searchByKeyword(String keyword, Long lastStoreId, StoreSearchCond condition, Pageable pageable)
    {
        List<Store> results = query.selectFrom(store)
                .where(

                        // 관리자가 승인한 가게만 보여야 한다.
                        store.isAssigned.eq(true),
                        // 검색 로직
                        store.name.contains(keyword),
                        // no-offset 방식
                        ltStoreId(lastStoreId),
                        // Category 중복 필터링
                        eqCategory(condition.getCategoryIds()),
                        // Convenience 중복 필터링
                        eqConvenience(condition.getConvenienceIds())
                )
                .orderBy(store.id.desc())
                .limit(pageable.getPageSize()+1) // 나는 5개 요청해도 쿼리상 +시켜서 6개 들고 오게 함
                .fetch();

        return checkLastPage(pageable, results);
    }


    // 실제로 관리자 승인 받은 가게 목록 중에서만 보이게 해야 한다

    /**
     * 로그인 유저가 북마크한 가게 목록 조회
     */
    public Slice<Store> getBookmarkedStore(String userId, Long lastStoreId, Pageable pageable)
    {
        List<Store> results = query
                .selectFrom(store)
                .leftJoin(store.bookMarks, bookMark)
                .where(
                        store.isAssigned.eq(true),
                        ltStoreId(lastStoreId),
                        bookMark.user.userId.eq(userId)
                )
                .orderBy(store.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }


    /**
     * 가게 상세 페이지 조회
     */
    public Store getStoreDetail(Long storeId)
    {
        return query.selectFrom(store)
                    .leftJoin(store.storeImages, storeImage)
                    .fetchJoin()
                    .where(store.id.eq(storeId))
                    .distinct()
                    .fetchOne();
    }

    /**
     * 로그인한 유저가 등록한 가게 목록 조회
     */
    public List<Store> getStoreCurrentUserAssigned(List<Long> storeIds)
    {
        return query.selectFrom(store)
                    .where(store.id.in(storeIds))
                    .fetch();

    }


    // 카테고리 필터링
    private BooleanBuilder eqCategory(List<Long> categoryIds)
    {
        if(categoryIds == null)
        {
            return null;
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 클라이언트가 넘긴 카테고리 목록을 다 가지고 있어야 true값 나옴
        for (Long categoryId : categoryIds) {
            booleanBuilder.and(store.categoryIds.contains(categoryId));
        }
        return booleanBuilder;
    }

    // 편의 기능 필터링
    private BooleanBuilder eqConvenience(List<Long> convenienceIds)
    {
        if(convenienceIds == null)
        {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (Long convenienceId : convenienceIds) {
            booleanBuilder.and(store.convenienceIds.contains(convenienceId));
        }
        return booleanBuilder;
    }

    // 무한 스크롤 구현 시 첫페이지는 null로 조건이 들어오는 케이스
    private BooleanExpression ltStoreId(Long storeId) {
        if (storeId == null) {
            return null;
        }

        return store.id.lt(storeId);
    }

    private Slice<Store> checkLastPage(Pageable pageable, List<Store> results) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
