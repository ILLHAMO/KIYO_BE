package project.kiyobackend.store;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.bookmark.application.BookmarkService;
import project.kiyobackend.common.WithAuthUser;
import project.kiyobackend.store.domain.domain.store.Comment;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

import java.util.Arrays;


@SpringBootTest
public class StoreControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    BookmarkService bookmarkService;

    @BeforeEach
    public void setup()
    {
        mvc= MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

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
    }

    @AfterEach
    public void destroy()
    {
        userRepository.deleteAll();
        userRepository.flush();
    }

  @Test
  @DisplayName("전체 Store 데이터 조회 시 인증 유저의 북마크가 있으면 booked 속성 true로 반환")
  @WithAuthUser(userId = "jemin")
  @Transactional
  public void test() throws Exception
  {
      mvc.perform(MockMvcRequestBuilders.get("/api/stores?lastStoreId=20&size=10")
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andDo(MockMvcResultHandlers.print());
  }

}
