package project.kiyobackend.store.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import project.kiyobackend.common.factory.StoreFactory;
import project.kiyobackend.common.factory.UserFactory;
import project.kiyobackend.store.adapter.infrastructure.AWSS3UploadService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.store.query.StoreQueryRepository;
import project.kiyobackend.store.query.StoreSearchCond;
import project.kiyobackend.user.domain.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreQueryRepository storeQueryRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private AWSS3UploadService uploadService;

    @DisplayName("사용자가 북마크 한 경우, 전체 데이터 조회 시 isBooked가 true, 북마크 안했으면 false")
    @Test
    void user_bookmarked()
    {
        // given
        User user = UserFactory.createUser(1L, "jemin");
        Store store1 = StoreFactory.createStore(1L);
        Store store2 = StoreFactory.createStore(2L);
        store1.addBookmark(user);
        given(storeQueryRepository.searchBySlice(anyLong(),any(StoreSearchCond.class), any(Pageable.class)))
                .willReturn(new SliceImpl<Store>(List.of(store1,store2)));

        // when
        Slice<Store> result = storeService.getStore(user, 3L, new StoreSearchCond(), PageRequest.ofSize(4));

        // then
        Assertions.assertThat(result.getContent().get(0).isBooked()).isTrue();
        Assertions.assertThat(result.getContent().get(1).isBooked()).isFalse();


    }

}