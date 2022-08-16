package project.kiyobackend.store.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import project.kiyobackend.common.factory.MockStore;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;

import javax.persistence.EntityManager;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreQueryRepositoryTest {

    @Autowired
    EntityManager em;

    StoreQueryRepository storeQueryRepository;

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    public void init()
    {
        storeQueryRepository = new StoreQueryRepository(em);

        for(int i = 0 ; i < 30; i++)
        {
            storeRepository.save(new MockStore.Builder().name("가게"+i+1).build());
        }

    }
    @AfterEach
    public void clear()
    {
        storeRepository.deleteAll();
    }

    @Test
    @DisplayName("No-Offset 방식을 사용하면 lastStoreId값 -1 부터 page size 만큼 가져옴")
    void test()
    {
        // given
        Slice<Store> stores = storeQueryRepository.searchBySlice(10L,
                                                                 new StoreSearchCond(),
                                                                PageRequest.ofSize(6));
        // when
        Long first = stores.getContent().get(0).getId();
        Long last  = stores.getContent().get(5).getId();

        // then
        Assertions.assertThat(first).isEqualTo(9);
        Assertions.assertThat(last).isEqualTo(4);

    }

    @Test
    @DisplayName("마지막 페이지에서는 isLast가 true, 마지막이 아니면 isLast가 false")
    void checkLast()
    {
        // given
        Slice<Store> getLastPage = storeQueryRepository.searchBySlice(10L,
                new StoreSearchCond(),
                PageRequest.ofSize(9));

        Slice<Store> getMiddlePage = storeQueryRepository.searchBySlice(10L,
                new StoreSearchCond(),
                PageRequest.ofSize(4));

        // when
        boolean isLastPage = getLastPage.isLast();
        boolean isNotLastPage = getMiddlePage.isLast();

        // then
        Assertions.assertThat(isLastPage).isTrue();
        Assertions.assertThat(isNotLastPage).isFalse();
    }

    @Test
    @DisplayName("lastStoreId가 null값으로 들어오면 가장 최근 데이터부터 가져옴")
    void check_if_lastStoreId_is_null()
    {
        // given
        Slice<Store> getPageWithoutStoreId = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(),
                PageRequest.ofSize(5));

        // when
        Long first = getPageWithoutStoreId.getContent().get(0).getId();
        Long last = getPageWithoutStoreId.getContent().get(4).getId();

        // then
        Assertions.assertThat(first).isEqualTo(30);
        Assertions.assertThat(last).isEqualTo(26);
    }

    @Test
    @DisplayName("categoryId로 필터링 테스트")
    void filter_by_categoryIds()
    {
        // given
        Store store1 = storeRepository.findById(28L).orElseThrow(NotExistStoreException::new);
        Store store2 = storeRepository.findById(29L).orElseThrow(NotExistStoreException::new);

        store1.getCategoryIds().addAll(List.of(1L,2L));
        store2.getCategoryIds().addAll(List.of(1L));

        em.flush();

        // when
        Slice<Store> filterByCategoryId = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L,2L),null),
                PageRequest.ofSize(5));

        Slice<Store> filterByCategoryId2 = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L),null),
                PageRequest.ofSize(5));

        // then
        Long result = filterByCategoryId.getContent().get(0).getId();
        Assertions.assertThat(result).isEqualTo(28L);

        // filter로 1L을 넣었을땐 1L와 1L,2L가 둘 다 나와야 한다.
        Assertions.assertThat(filterByCategoryId2.getContent().size()).isEqualTo(2);
        Assertions.assertThat(filterByCategoryId2.getContent().get(1).getId()).isEqualTo(28L);

    }

    @Test
    @DisplayName("convenienceId로 필터링 테스트")
    void filter_by_convenienceIds()
    {
        // given
        Store store1 = storeRepository.findById(28L).orElseThrow(NotExistStoreException::new);
        Store store2 = storeRepository.findById(29L).orElseThrow(NotExistStoreException::new);

        store1.getConvenienceIds().addAll(List.of(1L,2L));
        store2.getConvenienceIds().addAll(List.of(1L));

        em.flush();

        // when
        Slice<Store> filterByConvenienceId = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L,2L),null),
                PageRequest.ofSize(5));

        Slice<Store> filterByConvenienceId2 = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L),null),
                PageRequest.ofSize(5));

        // then
        Long result = filterByConvenienceId.getContent().get(0).getId();
        Assertions.assertThat(result).isEqualTo(28L);

        // filter로 1L을 넣었을땐 1L와 1L,2L가 둘 다 나와야 한다.
        Assertions.assertThat(filterByConvenienceId2.getContent().size()).isEqualTo(2);
        Assertions.assertThat(filterByConvenienceId2.getContent().get(1).getId()).isEqualTo(28L);

    }

    @Test
    @DisplayName("convenienceId와 categoryId 함께 필터링 테스트")
    void filter_by_convenienceIds_and_categoryIds()
    {
        // given
        Store store1 = storeRepository.findById(28L).orElseThrow(NotExistStoreException::new);
        Store store2 = storeRepository.findById(29L).orElseThrow(NotExistStoreException::new);

        store1.getConvenienceIds().addAll(List.of(1L,2L));
        store2.getConvenienceIds().addAll(List.of(1L,3L));

        store1.getCategoryIds().addAll(List.of(1L,2L));
        store2.getCategoryIds().addAll(List.of(1L,3L));

        em.flush();

        // when
        Slice<Store> filterByConvenienceId = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L,2L),List.of(1L,2L)),
                PageRequest.ofSize(5));

        Slice<Store> filterByConvenienceId2 = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L,3L),List.of(1L)),
                PageRequest.ofSize(5));

        Slice<Store> filterByConvenienceId3 = storeQueryRepository.searchBySlice(null,
                new StoreSearchCond(List.of(1L),List.of(1L)),
                PageRequest.ofSize(5));

        // then
        Long result = filterByConvenienceId.getContent().get(0).getId();
        Assertions.assertThat(result).isEqualTo(28L);

        // filter로 1L을 넣었을땐 1L와 1L,2L가 둘 다 나와야 한다.
        Assertions.assertThat(filterByConvenienceId2.getContent().get(0).getId()).isEqualTo(29L);

        Assertions.assertThat(filterByConvenienceId3.getContent().size()).isEqualTo(2);
    }
}