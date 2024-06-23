package my.unishop.global.jwt.repository;

import my.unishop.global.jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    void deleteByToken(String refreshToken);
    RefreshToken findByToken(String refreshToken);
}
