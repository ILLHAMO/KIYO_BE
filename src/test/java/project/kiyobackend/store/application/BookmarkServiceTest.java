package project.kiyobackend.store.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.kiyobackend.common.factory.StoreFactory;
import project.kiyobackend.common.factory.UserFactory;
import project.kiyobackend.exception.store.NotExistStoreException;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreRepository;
import project.kiyobackend.user.domain.User;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private StoreRepository storeRepository;

    @DisplayName("사용자가 북마크 누르면 북마크 추가된다.")
    //@Test
    void add_bookmark()
    {
        // given
        Long storeId = 1L;
        User user = UserFactory.user(1L, "jemin");
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(StoreFactory.store(storeId)));

        // when
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.addBookmark(user, storeId);

        // then
        Assertions.assertThat(bookmarkResponseDto.getBookmarkCount()).isEqualTo(1);
        Assertions.assertThat(bookmarkResponseDto.getIsBooked()).isTrue();

    }

    @DisplayName("사용자가 북마크 다시 누르면 북마크 해제된다.")
   // @Test
    void remove_bookmark()
    {
        // given
        Long storeId = 1L;
        User user = UserFactory.user(1L, "jemin");

        Store store = StoreFactory.createStore(storeId);
        store.addBookmark(user);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        // when
        // TODO : remove 사용하려면 equal과 hashcode를 조절해줘야함!
        BookmarkResponseDto bookmarkResponseDto = bookmarkService.removeBookmark(user, storeId);
        // then
        Assertions.assertThat(bookmarkResponseDto.getIsBooked()).isFalse();
        Assertions.assertThat(bookmarkResponseDto.getBookmarkCount()).isEqualTo(0);

    }

}