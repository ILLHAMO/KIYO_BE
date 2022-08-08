package project.kiyobackend.store.adapter.presentation.dto;

import org.springframework.data.domain.Slice;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreDetailResponse;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.adapter.presentation.dto.store.StoreResponse;
import project.kiyobackend.store.adapter.presentation.dto.store.UserBookmarkResponse;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.application.dto.UserBookmarkResponseDto;
import project.kiyobackend.store.domain.domain.store.Store;

import java.util.List;
import java.util.stream.Collectors;

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
                s.getReviewCounts(),
                s.getBookmarkCounts(),
                s.isBooked()));
    }

    public static StoreDetailResponse storeDetailResponse(StoreDetailResponseDto storeDetailResponseDto)
    {
        return StoreDetailResponse.builder().name(storeDetailResponseDto.getName()).kids(storeDetailResponseDto.isKids())
                .isBooked(storeDetailResponseDto.isBooked())
                .simpleComment(storeDetailResponseDto.getSimpleComment())
                .tags(storeDetailResponseDto.getTag())
                .address(storeDetailResponseDto.getAddress())
                .time(storeDetailResponseDto.getTime())
                .detailComment(storeDetailResponseDto.getDetailComment())
                .addressMap(storeDetailResponseDto.getAddressMap())
                .images(storeDetailResponseDto.getImages())
                .convenienceIds(storeDetailResponseDto.getConvenienceIds())
                .menus(storeDetailResponseDto.getMenuResponses())
                .reviews(storeDetailResponseDto.getReviewResponses())
                .build();

    }

    public static StoreDetailResponseDto storeDetailResponseDto(Store findStore)
    {
        return StoreDetailResponseDto.builder().name(findStore.getName()).kids(findStore.isKids())
                .isBooked(findStore.isBooked())
                .simpleComment(findStore.getComment().getSimpleComment())
                .tags(findStore.getTagStores().stream().map(ts -> ts.getTag()).collect(Collectors.toList()))
                .address(findStore.getAddress())
                .time(findStore.getTime().stream().map(t -> t.getTime()).collect(Collectors.toList()))
                .detailComment(findStore.getComment().getDetailComment())
                .addressMap(findStore.getAddressMap())
                .images(findStore.getStoreImages())
                .convenienceIds(findStore.getConvenienceIds())
                .menus(findStore.getMenus())
                .reviews(findStore.getReviews())
                .build();
    }

    public static Slice<UserBookmarkResponseDto> userBookmarkResponseDto(Slice<Store> store)
    {
        return store.
                map(b ->
                        new UserBookmarkResponseDto(b.getId(),
                                b.isKids(),
                                b.getStoreImages(),
                                b.getName(),
                                b.getAddress(),
                                b.getReviewCounts(),
                                b.getBookmarkCounts()));
    }


    public static Slice<UserBookmarkResponse> userBookmarkResponse(Slice<UserBookmarkResponseDto> store)
    {
        return store.map(u ->
                new UserBookmarkResponse(u.getId(),
                        u.isKids(),
                        u.getImages(),
                        u.getName(),
                        u.getAddress(),
                        u.getReviewCount(),
                        u.getBookmarkCount(),
                        u.isBooked()));
    }
}
