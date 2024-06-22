package my.unishop.global.jwt.service;

import lombok.RequiredArgsConstructor;
import my.unishop.global.jwt.JwtUtil;
import my.unishop.global.jwt.repository.RefreshTokenRepository;
import my.unishop.global.jwt.dto.AuthResponse;
import my.unishop.global.jwt.entity.RefreshToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authenticatedUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        String accessToken = jwtUtil.generateAccessToken(authenticatedUsername);
        String refreshToken = jwtUtil.generateRefreshToken(authenticatedUsername);

        refreshTokenRepository.save(new RefreshToken(authenticatedUsername, refreshToken));

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token이 유효하지 않습니다.");
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken);

        if (storedToken != null && storedToken.getUsername().equals(username)) {
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return new AuthResponse(newAccessToken, null);
        }
        throw new IllegalArgumentException("Refresh token이 존재하지 않습니다.");
    }

    public void logout(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            refreshTokenRepository.deleteByToken(refreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }
}
