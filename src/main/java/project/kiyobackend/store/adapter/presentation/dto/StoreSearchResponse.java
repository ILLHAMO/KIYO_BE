package project.kiyobackend.store.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.kiyobackend.user.adapter.presentation.dto.StoreImageResponseDto;

@Data
@AllArgsConstructor
public class StoreSearchResponse {
    private Long id;
    private String name;
    private String address;
    private StoreImageResponseDto storeImage;
}
