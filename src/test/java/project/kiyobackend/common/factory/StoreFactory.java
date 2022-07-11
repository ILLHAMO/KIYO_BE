package project.kiyobackend.common.factory;

import project.kiyobackend.category.domain.CategoryId;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;

import java.util.List;

public class StoreFactory {

    private StoreFactory()
    {

    }
    public static Store store(Long storeId)
    {
        return createStore(storeId);
    }

    public static Store createStore(Long storeId)
    {
        return MockStore.builder()
                .id(storeId)
                .build();
    }

    public static Store createStoreByCategoryId(List<Long> categoryIds)
    {
        return MockStore.builder().categoryIds(categoryIds).build();
    }

    public static Store createStoreByConvenienceId(List<Long> convenienceIds)
    {
        return MockStore.builder().convenienceIds(convenienceIds).build();
    }

    public static Store createStoreByCategoryIdAndConvenienceId(List<Long> categoryIds,List<Long> convenienceIds)
    {
        return MockStore.builder().categoryIds(categoryIds).convenienceIds(convenienceIds).build();

    }
}
