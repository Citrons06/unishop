package my.unishop.jwt.repository;

import my.unishop.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByToken(String refreshToken);
    RefreshToken findByToken(String refreshToken);
}
