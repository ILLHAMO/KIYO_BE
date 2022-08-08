package project.kiyobackend.user.adapter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreCurrentUserAssignedResponseDto {

    private Long storeId;
    private String name;
    private String address;
    private boolean isAssigned;


}
