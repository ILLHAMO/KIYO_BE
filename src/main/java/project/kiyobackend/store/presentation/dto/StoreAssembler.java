package project.kiyobackend.store.presentation.dto;

import org.springframework.data.domain.Slice;
import project.kiyobackend.admin.store.presentation.dto.StoreDetailResponseForUpdate;
import project.kiyobackend.store.presentation.dto.menu.MenuOptionResponseDto;
import project.kiyobackend.store.presentation.dto.menu.MenuResponseDto;
import project.kiyobackend.store.presentation.dto.review.ReviewResponseDto;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponse;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.presentation.dto.store.StoreResponse;
import project.kiyobackend.store.presentation.dto.store.UserBookmarkResponse;
import project.kiyobackend.store.application.dto.BookmarkResponseDto;
import project.kiyobackend.store.application.dto.UserBookmarkResponseDto;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.adapter.presentation.dto.StoreImageResponseDto;

import java.util.Comparator;
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

    public static Slice<StoreSearchResponse> storeSearchResponses(Slice<Store> stores)
    {
        return stores.map(s-> new StoreSearchResponse(s.getId(),
                s.getName(),
                s.isKids(),
                s.getAddress(),
                s.isBooked(),
                s.getReviewCounts(),
                s.getBookmarkCounts(),
                s.getStoreImages().stream().map(si -> new StoreImageResponseDto(si.getId(),si.getPath())).collect(Collectors.toList()).get(0)));
    }

    public static StoreDetailResponse storeDetailResponse(StoreDetailResponseDto storeDetailResponseDto)
    {
        return StoreDetailResponse.builder().name(storeDetailResponseDto.getName()).kids(storeDetailResponseDto.isKids())
                .isBooked(storeDetailResponseDto.isBooked())
                .call(storeDetailResponseDto.getCall())
                .simpleComment(storeDetailResponseDto.getSimpleComment())
                .tags(storeDetailResponseDto.getTag())
                .address(storeDetailResponseDto.getAddress())
                .time(storeDetailResponseDto.getTime())
                .detailComment(storeDetailResponseDto.getDetailComment())
                .addressMap(storeDetailResponseDto.getAddressMap())
                .images(storeDetailResponseDto.getImages())
                .conveniences(storeDetailResponseDto.getConvenienceIds())
                .menus(storeDetailResponseDto.getMenuResponses())
                .reviews(storeDetailResponseDto.getReviewResponses().stream().sorted(Comparator.comparing(ReviewResponseDto::getUpdatedDate).reversed()).collect(Collectors.toList()))
                .build();

    }

    public static StoreDetailResponseDto storeDetailResponseDto(Store findStore)
    {
        return StoreDetailResponseDto.builder().name(findStore.getName()).kids(findStore.isKids())
                .isBooked(findStore.isBooked())
                .call(findStore.getCall())
                .simpleComment(findStore.getComment().getSimpleComment())
                .tags(findStore.getTagStores().stream().map(ts -> ts.getTag()).collect(Collectors.toList()))
                .address(findStore.getAddress())
                .time(findStore.getTime().stream().map(t -> t.getTime()).collect(Collectors.toList()))
                .detailComment(findStore.getComment().getDetailComment())
                .addressMap(findStore.getAddressMap())
                .images(findStore.getStoreImages())
                .menus(findStore.getMenus())
                .reviews(findStore.getReviews())
                .build();
    }

    public static StoreDetailResponseForUpdate storeDetailResponseForUpdate(Store findStore)
    {
        return StoreDetailResponseForUpdate.builder().name(findStore.getName()).kids(findStore.isKids())
                .isBooked(findStore.isBooked())
                .simpleComment(findStore.getComment().getSimpleComment())
                .tags(findStore.getTagStores().stream().map(ts -> ts.getTag()).collect(Collectors.toList()))
                .address(findStore.getAddress())
                .time(findStore.getTime().stream().map(t -> t.getTime()).collect(Collectors.toList()))
                .detailComment(findStore.getComment().getDetailComment())
                .addressMap(findStore.getAddressMap())
                .images(findStore.getStoreImages().stream().map(si -> new ImageDto(si.getId(),si.getPath())).collect(Collectors.toList()))
                .convenienceIds(findStore.getConvenienceIds())
                .menus(findStore.getMenus().stream().map(m-> new MenuResponseDto(m.getId(),m.getName(),m.getMenuOptions().stream().map(mo->new MenuOptionResponseDto(mo.getId(),mo.getName())).collect(Collectors.toList())
                )).collect(Collectors.toList()))
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
