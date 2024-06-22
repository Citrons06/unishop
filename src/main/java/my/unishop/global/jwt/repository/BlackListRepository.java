package my.unishop.global.jwt.repository;

import my.unishop.global.jwt.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    void deleteByCreatedDateBefore(LocalDateTime expiryDate);
    boolean existsByInvalidRefreshToken(String token);
}
