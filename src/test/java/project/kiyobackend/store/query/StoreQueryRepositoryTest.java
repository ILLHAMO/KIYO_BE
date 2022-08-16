package project.kiyobackend.store.query;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


// datajpatest는 결국에는 정식 스프링 빈들 띄워야됨. 그러면 테스트 환경이 아니라, 실제 db 관련 세팅 해줘야 함.
@DataJpaTest
class StoreQueryRepositoryTest {

    @Test
    void test()
    {

    }

}