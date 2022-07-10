package project.kiyobackend.store.query;



//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreQueryRepositoryTest {


//    @Autowired StoreRepository storeRepository;
//    @Autowired StoreQueryRepository storeQueryRepository;
//
//    // JpaQueryFactory는 @DataJpaTest에서 자동 빈 등록 되지 않기 때문에 따로 빈 등록 해줘야 한다.
//    @TestConfiguration
//    static class TestConfig{
//        @Bean
//        public StoreQueryRepository storeQueryRepository(EntityManager em)
//        {
//            return new StoreQueryRepository(em);
//        }
//    }
//
//    @BeforeEach
//    public void addStore()
//    {
//        for(int i = 0; i < 10; i++)
//        {
//            storeRepository.save(new Store(
//                    "jemin"+i,
//                    "1234",
//                    new Comment("hello","hi")
//                    ,"2020","창원시 상남동",
//                    true, List.of(1L,2L), List.of(3L,4L)));
//        }
//    }
//
//    @Test
//    @DisplayName("마지막 페이지가 아니라면 hasNext가 true로 표시된다.")
//    void is_not_final()
//    {
//        //given
//        PageRequest pageRequest = PageRequest.ofSize(3);
//        StoreSearchCond storeSearchCond = new StoreSearchCond();
//        // when
//        Slice<Store> stores = storeQueryRepository.searchBySlice(7L, storeSearchCond, pageRequest);
//        // then
//        Assertions.assertThat(stores.isLast()).isFalse();
//    }
//
//    @Test
//    @DisplayName("마지막 페이지라면 hasNext가 false로 표시된다.")
//    void is_final()
//    {
//        //given
//        PageRequest pageRequest = PageRequest.ofSize(4);
//        StoreSearchCond storeSearchCond = new StoreSearchCond();
//        // when
//        Slice<Store> stores = storeQueryRepository.searchBySlice(5L, storeSearchCond, pageRequest);
//        // then
//        Assertions.assertThat(stores.isLast()).isTrue();
//
//
//    }

}