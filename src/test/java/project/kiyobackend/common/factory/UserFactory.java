package project.kiyobackend.common.factory;

import project.kiyobackend.user.domain.User;

public class UserFactory {

    private UserFactory()
    {
    }

    public static User user(String userId)
    {
         return createUser(userId);
    }

    public static User createUser(String userId)
    {
       return MockUser.builder()
               .userId(userId)
               .build();
    }
}
