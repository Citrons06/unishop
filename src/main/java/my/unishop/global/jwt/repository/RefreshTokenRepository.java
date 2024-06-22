package my.unishop.global.jwt.repository;

import my.unishop.global.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByToken(String refreshToken);
    RefreshToken findByToken(String refreshToken);
}
