package project.kiyobackend.store;




//@SpringBootTest
public class StoreControllerTest {

//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mvc;
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    StoreRepository storeRepository;
//    @Autowired
//    BookmarkService bookmarkService;
//
//    @BeforeEach
//    public void setup()
//    {
//        mvc= MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
//                .build();
//
//        for(int i = 0; i < 100; i++)
//        {
//            storeRepository.save(new Store("상점"+i,
//                    "010-2757-2345",
//                    new Comment("simple", "detail"),
//                    "20:00", true,
//                    Arrays.asList(1L, 2L),
//                    Arrays.asList(3L, 4L)));
//        }
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();
//        // 현재 사용자 정보
//        User user = principal.getUser();
//
//        for(int j = 0; j < 30; j++)
//        {
//            bookmarkService.addLike(user,2L*j+1l);
//        }
//    }
//
//    @AfterEach
//    public void destroy()
//    {
//        userRepository.deleteAll();
//        userRepository.flush();
//    }

//  @Test
//  @DisplayName("전체 Store 데이터 조회 시 인증 유저의 북마크가 있으면 booked 속성 true로 반환")
//  @WithAuthUser(userId = "jemin")
//  @Transactional
//  public void test() throws Exception
//  {
//      mvc.perform(MockMvcRequestBuilders.get("/api/stores?lastStoreId=20&size=10")
//              .contentType(MediaType.APPLICATION_JSON))
//              .andExpect(MockMvcResultMatchers.status().isOk())
//              .andDo(MockMvcResultHandlers.print());
//  }

}
