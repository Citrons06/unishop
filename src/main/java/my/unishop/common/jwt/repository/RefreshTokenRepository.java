package my.unishop.common.jwt.repository;

import my.unishop.common.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    void deleteByToken(String refreshToken);
    RefreshToken findByToken(String refreshToken);
}
