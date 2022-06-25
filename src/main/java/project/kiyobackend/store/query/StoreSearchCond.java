package project.kiyobackend.store.query;

import lombok.Data;
import project.kiyobackend.category.domain.CategoryId;
import project.kiyobackend.convenience.domain.ConvenienceId;

import java.util.List;

@Data
public class StoreSearchCond {

    private List<Long> categoryIds;
    private List<Long> convenienceIds;

}
