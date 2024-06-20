package my.unishop.jwt.repository;

import my.unishop.jwt.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    void deleteByCreatedDateBefore(LocalDateTime expiryDate);
}
