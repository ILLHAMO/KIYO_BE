package project.kiyobackend.store.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import project.kiyobackend.common.factory.MockStore;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;

import javax.persistence.EntityManager;


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

}