package project.kiyobackend.store.query;

import lombok.Data;


import java.util.List;

@Data
public class StoreSearchCond {

    private List<Long> categoryIds;
    private List<Long> convenienceIds;

}
