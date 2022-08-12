package project.kiyobackend.user.adapter.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreCurrentUserAssignedResponseDto {

    @Schema(example = "21")
    private Long storeId;
    @Schema(example = "이모네 생고기")
    private String name;
    @Schema(example = "성동구 왕십리")
    private String address;
    private StoreImageResponseDto storeImage;
    @Schema(example = "false")
    private boolean isAssigned;


}
