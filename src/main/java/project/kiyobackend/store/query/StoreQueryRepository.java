package project.kiyobackend.store.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import project.kiyobackend.store.domain.domain.store.Store;
import javax.persistence.EntityManager;
import java.util.List;
import static project.kiyobackend.store.domain.domain.store.QStore.store;



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
     * Slice를 사용하면 count 쿼리를 생략함으로써 성능 최적화 가능
     * @param lastStoreId 페이징 최적화 기법, 이전에 조회한 id중 가장 마지막을 제공하고 그 다음 id 부터 다시 조회
     * @param condition  카테고리, 서비스 필터링 동적 쿼리
     * @param pageable   size 정보 들어있음
     * @return store를 Slice 타입으로 반환
     */
    public Slice<Store> searchBySlice(Long lastStoreId,String username, StoreSearchCond condition, Pageable pageable)
    {
        List<Store> results = query.selectFrom(store)
                .where(
                        // 내가
                        // 이전 페이지 마지막 id값을 사용한 무한 스크롤 최적화
                        ltStoreId(lastStoreId),
                        // Category 중복 필터링
                        eqCategory(condition.getCategoryIds()),
                        // Convenience 중복 필터링
                        eqConvenience(condition.getConvenienceIds())
                )
                .orderBy(store.id.desc())
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


    private BooleanBuilder eqCategory(List<Long> categoryIds)
    {
        if(categoryIds == null)
        {
            return null;
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (Long categoryId : categoryIds) {
            booleanBuilder.and(store.categoryIds.contains(categoryId));
        }
        return booleanBuilder;
    }


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


    private BooleanExpression ltStoreId(Long storeId) {
        if (storeId == null) {
            return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
        }

        return store.id.lt(storeId);
    }



//    public List<StoreResponseDto> searchByPageGroupBy(StoreSearchCond condition, Pageable pageable)
//    {
//        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
//        Map<Store, List<StoreImage>> transform = query.from(store)
//                .leftJoin(store.storeImages, storeImage)
//                .where(
//                        // Category 중복 필터링
//                        eqCategory(condition.getCategoryIds()),
//                        // Convenience 중복 필터링
//                        eqConvenience(condition.getConvenienceIds())
//                )
//                .transform(groupBy(store).as(GroupBy.list(storeImage)));
//
//        return transform.entrySet().stream()
//                .map(entry-> new StoreResponseDto(entry.getKey().getId(),entry.getKey().isKids(),entry.getValue(),entry.getKey().getName(),entry.getKey().getReviewCount(),entry.getKey().getBookmarkCount()))
//                .collect(Collectors.toList());
//    }
//
//    public Page<StoreResponseDto> searchByPageNotwork(StoreSearchCond condition, Pageable pageable)
//    {
//        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
//        QueryResults<StoreResponseDto> results = query
//                .select(new QStoreResponseDto(store.id, store.isKids, store.storeImages, store.name, store.reviewCount, store.bookmarkCount))
//                .from(store)
//                .where(
//                        // Category 중복 필터링
//                        eqCategory(condition.getCategoryIds()),
//                        // Convenience 중복 필터링
//                        eqConvenience(condition.getConvenienceIds())
//                )
//                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        List<StoreResponseDto> content = results.getResults();
//        long total = results.getTotal();
//        return new PageImpl<>(content,pageable,total);
//
//    }



}
