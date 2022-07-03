package project.kiyobackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.kiyobackend.auth.entity.UserPrincipal;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 실제 저장소에서 사용자를 찾는다.
        User user = userRepository.findByUserId(username);
        // 사용자가 없다면 예외 발생
        if (user == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }
        // UserDetails를 구현한 UserPrincipal 사용
        return UserPrincipal.create(user);
    }
}