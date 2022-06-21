package project.kiyobackend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.kiyobackend.auth.token.UserRefreshToken;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken,Long> {
    UserRefreshToken findByUserId(String userId);
    UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
