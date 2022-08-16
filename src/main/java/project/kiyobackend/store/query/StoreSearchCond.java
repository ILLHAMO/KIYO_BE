package project.kiyobackend.store.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreSearchCond {

    private List<Long> categoryIds;
    private List<Long> convenienceIds;

}
