package project.kiyobackend.store.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import project.kiyobackend.store.domain.domain.store.Comment;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import javax.persistence.EntityManager;
import java.util.List;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreQueryRepositoryTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired StoreQueryRepository storeQueryRepository;



    @TestConfiguration
    static class TestConfig{

        @Bean
        public StoreQueryRepository storeQueryRepository(EntityManager em)
        {
            return new StoreQueryRepository(em);
        }
    }

    @BeforeEach
    public void addStore()
    {
        for(int i = 0; i < 100; i++)
        {
            storeRepository.save(new Store("jemin"+i,"1234",new Comment("hello","hi"),"2020",
                    true, List.of(1L,2L),List.of(3L,4L)));
        }
    }

    @Test
    @DisplayName("슬라이싱 테스트")
    void test()
    {
        //given
        PageRequest pageRequest = PageRequest.of(0, 4);
        StoreSearchCond storeSearchCond = new StoreSearchCond();
        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(5L, storeSearchCond, pageRequest);
        // then
        Assertions.assertThat(stores.hasNext()).isFalse();
        Assertions.assertThat(stores.getContent().get(2).getId()).isEqualTo(2L);
    }
}