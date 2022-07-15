package project.kiyobackend.store.adapter.presentation.dto;

import org.springframework.data.domain.Slice;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.application.dto.StoreDetailResponseDto;
import project.kiyobackend.store.domain.domain.store.Store;

public class StoreAssembler {

    public static BookmarkResponse bookmarkResponse(BookmarkResponseDto bookmarkResponseDto) {
        return BookmarkResponse.builder()
                .bookmarkCount(bookmarkResponseDto.getBookmarkCount())
                .isBooked(bookmarkResponseDto.getIsBooked())
                .build();
    }

    public static Slice<StoreResponse> storeResponseDto(Slice<Store> stores)
    {
        return stores.map(s -> new StoreResponse(s.getId(),
                s.isKids(),
                s.getStoreImages(),
                s.getName(),
                s.getAddress(),
                s.getReviewCount(),
                s.getBookmarkCounts(),
                s.isBooked()));
    }


}
