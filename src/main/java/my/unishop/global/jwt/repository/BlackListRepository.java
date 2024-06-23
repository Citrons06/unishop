package my.unishop.global.jwt.repository;

import my.unishop.global.jwt.entity.BlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, Long> {
    void deleteByCreatedDateBefore(LocalDateTime expiryDate);
    boolean existsByRefreshToken(String token);
}