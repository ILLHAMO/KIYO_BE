package project.kiyobackend.store.query.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StorePaginationDto {
    private Long id;
    private String name;
    private boolean isAssigned;
}
