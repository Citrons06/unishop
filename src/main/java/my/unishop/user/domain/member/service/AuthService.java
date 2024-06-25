package my.unishop.user.domain.member.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.util.JwtUtil;
import my.unishop.common.jwt.repository.RefreshTokenRepository;
import my.unishop.common.dto.AuthResponse;
import my.unishop.common.entity.RefreshToken;
import my.unishop.user.domain.member.dto.LoginRequestDto;
import my.unishop.user.domain.member.dto.LoginResponseDto;
import my.unishop.user.domain.member.entity.Member;
import my.unishop.user.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        try {
            Member member = memberRepository.findByUsername(username);

            if (!passwordEncoder.matches(password, member.getPassword())) {
                throw new IllegalArgumentException("회원 정보가 일치하지 않습니다.");
            }

            String accessToken = jwtUtil.generateAccessToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            refreshTokenRepository.save(new RefreshToken(username, refreshToken));

            response.addHeader("RefreshToken", refreshToken);
            response.addHeader("AccessToken", accessToken);

            LoginResponseDto loginResponseDto = new LoginResponseDto(username, accessToken, refreshToken);
            loginResponseDto.setUsername(username);
            loginResponseDto.setAccessToken(accessToken);
            loginResponseDto.setRefreshToken(refreshToken);

            return loginResponseDto;

        } catch (Exception e) {
            log.error("Error authenticating user", e);
            throw new IllegalArgumentException("회원 정보가 일치하지 않습니다.");
        }
    }

    public void logout(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            refreshTokenRepository.deleteByToken(refreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Claims claims = jwtUtil.getClaimsFromToken(refreshToken);
        String username = claims.getSubject();

        if (!refreshTokenRepository.existsByToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String accessToken = jwtUtil.generateAccessToken(username);
        return new AuthResponse(accessToken);
    }
}
