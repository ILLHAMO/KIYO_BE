package project.kiyobackend.common.factory;

import project.kiyobackend.user.domain.User;

public class UserFactory {

    private UserFactory()
    {
    }

    public static User user(Long userSeq, String userId)
    {
         return createUser(userSeq,userId);
    }

    public static User createUser(Long userSeq,String userId)
    {
       return MockUser.builder()
               .userSeq(userSeq)
               .userId(userId)
               .build();
    }
}
