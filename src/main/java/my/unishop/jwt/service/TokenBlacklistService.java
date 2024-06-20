package my.unishop.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.jwt.entity.BlackList;
import my.unishop.jwt.repository.BlackListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final BlackListRepository blackListRepository;

    public void blacklistToken(String token) {
        log.info("블랙리스트에 추가 : " + token);
        blackListRepository.save(new BlackList(token));
    }
}
