package project.kiyobackend.store.query;

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
import org.springframework.transaction.annotation.Transactional;
import project.kiyobackend.common.factory.StoreFactory;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;

import javax.persistence.EntityManager;


import java.util.List;


//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@Transactional
class StoreQueryRepositoryTest {

    @Autowired StoreQueryRepository storeQueryRepository;
    @Autowired StoreRepository storeRepository;
    // JpaQueryFactory는 @DataJpaTest에서 자동 빈 등록 되지 않기 때문에 따로 빈 등록 해줘야 한다.
    @TestConfiguration
    static class TestConfig{

        @Bean
        public StoreQueryRepository storeQueryRepository(EntityManager em)
        {
            return new StoreQueryRepository(em);
        }
    }

  //  @BeforeEach
    void init_data()
    {
         for (int i = 0 ; i < 20; i ++)
         {
             if(i%2 ==0)
             {
                 Store store = StoreFactory.createStoreByCategoryIdAndConvenienceId(List.of(1L,2L),List.of(2L));
                 storeRepository.save(store);
             }
             else{
                 Store store = StoreFactory.createStoreByCategoryIdAndConvenienceId(List.of(2L,3L),List.of(1L));
                 storeRepository.save(store);
             }
         }
         storeRepository.flush();
    }

    //@DisplayName("카테고리별로 필터링 구현")
    //@Test
    void filering_by_category()
    {
        // given
        StoreSearchCond storeSearchCond = new StoreSearchCond();
        storeSearchCond.setCategoryIds(List.of(1L,2L));

        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(null,storeSearchCond,PageRequest.of(0,20));

        // then
        org.assertj.core.api.Assertions.assertThat(stores.getContent().size()).isEqualTo(10);
    }

    //@DisplayName("편의서비스별로 필터링 구현")
    //@Test
    void filering_by_convenience_service()
    {
        // given
        StoreSearchCond storeSearchCond = new StoreSearchCond();
        storeSearchCond.setConvenienceIds(List.of(1L));

        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(null,storeSearchCond,PageRequest.of(0,20));

        // then
        org.assertj.core.api.Assertions.assertThat(stores.getContent().size()).isEqualTo(10);
    }

    //@DisplayName("카테고리와 편의 서비스 중복 필터링")
    //@Test
    void filering_by_convenience_service_and_category()
    {
        // given
        StoreSearchCond storeSearchCond = new StoreSearchCond();
        storeSearchCond.setConvenienceIds(List.of(1L));
        storeSearchCond.setCategoryIds(List.of(2L));

        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(null,storeSearchCond,PageRequest.of(0,20));

        // then
        org.assertj.core.api.Assertions.assertThat(stores.getContent().size()).isEqualTo(10);
    }

    //@DisplayName("마지막 페이지의 경우 isNext가 true")
    //@Test
    void is_final_page()
    {
        // given
        StoreSearchCond storeSearchCond = new StoreSearchCond();

        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(5L,storeSearchCond,PageRequest.of(0,4));

        // then
        org.assertj.core.api.Assertions.assertThat(stores.isLast()).isTrue();
    }

    //@DisplayName("마지막 페이지가 아닐 경우 isNext가 false")
    //@Test
    void is_not_final_page()
    {
        // given
        StoreSearchCond storeSearchCond = new StoreSearchCond();
        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(5L,storeSearchCond,PageRequest.ofSize(3));
        System.out.println(stores.isLast());
        // then
        org.assertj.core.api.Assertions.assertThat(stores.isLast()).isFalse();
    }

    //@DisplayName("조건 안맞으면 개수 0개 조회")
    //@Test
    void no_result()
    {
        // given
        StoreSearchCond storeSearchCond = new StoreSearchCond();
        storeSearchCond.setConvenienceIds(List.of(1L));
        storeSearchCond.setCategoryIds(List.of(1L));

        // when
        Slice<Store> stores = storeQueryRepository.searchBySlice(null,storeSearchCond,PageRequest.of(0,20));

        // then
        org.assertj.core.api.Assertions.assertThat(stores.getContent().size()).isEqualTo(0);
    }






}