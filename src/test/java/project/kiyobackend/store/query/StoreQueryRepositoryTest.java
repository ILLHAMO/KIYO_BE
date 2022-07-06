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


    @Autowired StoreRepository storeRepository;
    @Autowired StoreQueryRepository storeQueryRepository;

//    // JpaQueryFactory는 @DataJpaTest에서 자동 빈 등록 되지 않기 때문에 따로 빈 등록 해줘야 한다.
//    @TestConfiguration
//    static class TestConfig{
//        @Bean
//        public StoreQueryRepository storeQueryRepository(EntityManager em)
//        {
//            return new StoreQueryRepository(em);
//        }
//    }

//    @BeforeEach
//    public void addStore()
//    {
//        for(int i = 0; i < 100; i++)
//        {
//            storeRepository.save(new Store("jemin"+i,"1234",new Comment("hello","hi"),"2020",
//                    true, List.of(1L,2L),List.of(3L,4L)));
//        }
//    }

//
//
//    @Test
//    @DisplayName("마지막 페이지가 아니라면 hasNext가 true로 표시된다.")
//    void is_not_final()
//    {
//        //given
//        PageRequest pageRequest = PageRequest.of(0, 3);
//        StoreSearchCond storeSearchCond = new StoreSearchCond();
//        // when
//        Slice<Store> stores = storeQueryRepository.searchBySlice(5L, storeSearchCond, pageRequest);
//        // then
//        Assertions.assertThat(stores.hasNext()).isTrue();
//
//        // id값이 4L인 데이터부터 조회 해옴
//        Assertions.assertThat(stores.getContent().get(2).getId()).isEqualTo(2L);
//
//    }
//
//    @Test
//    @DisplayName("마지막 페이지라면 hasNext가 false로 표시된다.")
//    void is_final()
//    {
//        //given
//        PageRequest pageRequest = PageRequest.of(0, 6);
//        StoreSearchCond storeSearchCond = new StoreSearchCond();
//        // when
//        Slice<Store> stores = storeQueryRepository.searchBySlice(3L, storeSearchCond, pageRequest);
//        // then
//        Assertions.assertThat(stores.hasNext()).isFalse();
//        // id값이 2L인 데이터부터 조회 해옴
//        Assertions.assertThat(stores.getContent().get(0).getId()).isEqualTo(2L);
//
//    }

}