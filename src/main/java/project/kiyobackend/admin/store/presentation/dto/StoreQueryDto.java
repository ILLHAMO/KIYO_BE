package project.kiyobackend.admin.store.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class StoreQueryDto {
    private boolean assigned;
    private String search;
}
