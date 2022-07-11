package project.kiyobackend.common.factory;

import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;

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
}
