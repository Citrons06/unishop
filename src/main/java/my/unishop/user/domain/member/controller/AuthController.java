package my.unishop.user.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.global.jwt.dto.AuthResponse;
import my.unishop.global.jwt.dto.LoginRequestDto;
import my.unishop.global.jwt.service.AuthService;
import my.unishop.global.jwt.service.BlackListTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final BlackListTokenService tokenBlacklistService;

    @PostMapping("/user/login-page")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        log.info("Received refresh token: {}", refreshToken);

        try {
            authService.logout(refreshToken);
            tokenBlacklistService.blacklistToken(refreshToken);
            return ResponseEntity.ok("로그아웃 되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid refresh token", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            AuthResponse authResponse = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            log.error("Refresh token is invalid or does not exist", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}