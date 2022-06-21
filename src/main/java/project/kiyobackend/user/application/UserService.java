package project.kiyobackend.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}
