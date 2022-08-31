package project.kiyobackend.store.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.user.adapter.presentation.dto.StoreImageResponseDto;

@Data
@AllArgsConstructor
public class StoreSearchResponse {
    private Long id;
    private String name;
    private boolean kids;
    private String address;
    private boolean isBooked;
    private int reviewCount;
    private int bookmarkCount;
    private StoreImageResponseDto storeImage;
}
