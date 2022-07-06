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

        User user = userRepository.findByUserId(username);

        if (user == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }

        return UserPrincipal.create(user);
    }
}