package project.kiyobackend.store.query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.querydsl.QPageRequest;
import project.kiyobackend.store.domain.domain.store.Store;

import java.util.List;

@SpringBootTest
class StoreQueryRepositoryTest {

    @Autowired
    private StoreQueryRepository storeQueryRepository;


    @Test
    void getByPage()
    {
        QPageRequest page = QPageRequest.of(0, 20);
        StoreSearchCond storeSearchCond = new StoreSearchCond();

        Page<Store> stores = storeQueryRepository.searchByPage(storeSearchCond, page);
    }


}