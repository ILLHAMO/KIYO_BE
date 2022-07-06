package project.kiyobackend.bookmark.application;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import project.kiyobackend.common.WithAuthUser;
import project.kiyobackend.store.domain.domain.store.Comment;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;

import project.kiyobackend.user.domain.UserRepository;


import java.util.Arrays;


@SpringBootTest
class BookmarkControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired UserRepository userRepository;

    @BeforeEach
    void init()
    {
        mvc= MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // 테스트용 상점 정보
        storeRepository.save(new Store("상점1",
                "010-2757-2345",
                new Comment("simple", "detail"),
                "20:00", true,
                Arrays.asList(1L, 2L),
                Arrays.asList(3L, 4L)));
        storeRepository.save(new Store("상점2",
                "010-2757-2345",
                new Comment("simple", "detail"),
                "20:00", true,
                Arrays.asList(1L, 2L),
                Arrays.asList(3L, 4L)));


    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    @DisplayName("북마크 정상 등록 테스트")
    @WithAuthUser(userId = "jemin")
    @Transactional
    void bookmark_success_test() throws Exception
    {
        // given
        mvc.perform(MockMvcRequestBuilders.post("/api/bookmark/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("북마크 등록 실패 테스트")
    @WithAuthUser(userId = "jemin")
    @Transactional
    void bookmark_fail_test() throws Exception
    {
        // given
        mvc.perform(MockMvcRequestBuilders.post("/api/bookmark/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }





}