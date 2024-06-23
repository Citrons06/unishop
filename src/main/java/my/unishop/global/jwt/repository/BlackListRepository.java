package my.unishop.global.jwt.repository;

import my.unishop.global.jwt.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface BlackListRepository extends CrudRepository<BlackList, Long> {
    void deleteByCreatedDateBefore(LocalDateTime expiryDate);
    boolean existsByRefreshToken(String token);
}