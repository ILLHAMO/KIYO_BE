package project.kiyobackend.store.query;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;


import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import project.kiyobackend.store.domain.domain.store.QStoreImage;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreImage;
import project.kiyobackend.util.jpa.QueryDslUtil;
import project.kiyobackend.util.jpa.RepositorySliceHelper;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static org.springframework.util.ObjectUtils.isEmpty;
import static project.kiyobackend.store.domain.domain.store.QStore.store;
import static project.kiyobackend.store.domain.domain.store.QStoreImage.storeImage;

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

    /*
    동적 쿼리 구현
    페이징 쿼리 최적화 완료! -> 과정 블로그에 글 한번 써 보자.
     */
    public Page<Store> searchByPage(StoreSearchCond condition,Pageable pageable)
    {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        QueryResults<Store> results = query.selectFrom(store)
            //    .leftJoin(store.storeImages, storeImage)
                .where(
                        // Category 중복 필터링
                        eqCategory(condition.getCategoryIds()),
                        // Convenience 중복 필터링
                        eqConvenience(condition.getConvenienceIds())
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Store> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }

    public Slice<Store> searchBySlice(StoreSearchCond condition, Pageable pageable)
    {

        List<Store> results = query.selectFrom(store)
                .where(
                        // Category 중복 필터링
                        eqCategory(condition.getCategoryIds()),
                        // Convenience 중복 필터링
                        eqConvenience(condition.getConvenienceIds())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return RepositorySliceHelper.toSlice(results,pageable);
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


    /**
     * 이러면 페이징 처리 불가능한거 아닌가??
     */
    public List<StoreResponseDto> searchByPageGroupBy(StoreSearchCond condition, Pageable pageable)
    {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        Map<Store, List<StoreImage>> transform = query.from(store)
                .leftJoin(store.storeImages, storeImage)
                .where(
                        // Category 중복 필터링
                        eqCategory(condition.getCategoryIds()),
                        // Convenience 중복 필터링
                        eqConvenience(condition.getConvenienceIds())
                )
                .transform(groupBy(store).as(GroupBy.list(storeImage)));

        return transform.entrySet().stream()
                .map(entry-> new StoreResponseDto(entry.getKey().getId(),entry.getKey().isKids(),entry.getValue(),entry.getKey().getName(),entry.getKey().getReviewCount(),entry.getKey().getBookmarkCount()))
                .collect(Collectors.toList());
    }

    public Page<StoreResponseDto> searchByPageNotwork(StoreSearchCond condition, Pageable pageable)
    {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        QueryResults<StoreResponseDto> results = query
                .select(new QStoreResponseDto(store.id, store.isKids, store.storeImages, store.name, store.reviewCount, store.bookmarkCount))
                .from(store)
                .where(
                        // Category 중복 필터링
                        eqCategory(condition.getCategoryIds()),
                        // Convenience 중복 필터링
                        eqConvenience(condition.getConvenienceIds())
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<StoreResponseDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);



    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "id":
                        OrderSpecifier<?> createdDate = QueryDslUtil
                                .getSortedColumn(direction, store, "id");
                        ORDERS.add(createdDate);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }

}
