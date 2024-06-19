package my.unishop.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new HashSet<>();

    public void blacklistToken(String token) {
        log.info("블랙리스트에 추가 : " + token);
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
