package my.unishop.global.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.global.jwt.repository.BlackListRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlacklistTokenCleanupService {

    private final BlackListRepository blackListRepository;

    @Scheduled(cron = "0 0 0 * * ?") //매일 자정에 실행
    public void cleanUpExpiredTokens() {
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(7); //7일 이전 토큰 삭제

        log.info("오래된 토큰이 삭제됩니다. {}", expiryDate);
        blackListRepository.deleteByCreatedDateBefore(expiryDate);
    }
}
