package project.kiyobackend.admin.store.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreQueryDto {
    private String type;
    private String search;
}
