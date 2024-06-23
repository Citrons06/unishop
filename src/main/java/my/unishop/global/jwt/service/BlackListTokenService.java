package my.unishop.global.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.global.jwt.entity.BlackList;
import my.unishop.global.jwt.repository.BlackListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BlackListTokenService {

    private final BlackListRepository blackListRepository;

    public void blacklistToken(String token) {
        log.info("블랙리스트에 추가 : " + token);
        blackListRepository.save(new BlackList(token));
    }

    public boolean isBlacklisted(String token) {
        return blackListRepository.existsByRefreshToken(token);
    }
}
