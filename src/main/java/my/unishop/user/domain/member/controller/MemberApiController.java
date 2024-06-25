package my.unishop.user.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.dto.AuthResponse;
import my.unishop.user.domain.member.dto.LoginRequestDto;
import my.unishop.user.domain.member.service.AuthService;
import my.unishop.common.jwt.service.BlackListTokenService;
import my.unishop.user.domain.member.dto.MemberRequestDto;
import my.unishop.user.domain.member.dto.MemberResponseDto;
import my.unishop.user.domain.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final AuthService authService;
    private final BlackListTokenService tokenBlacklistService;

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.signup(memberRequestDto);
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body("{\"msg\" : \"인증 메일이 전송되었습니다.\"}");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token, @RequestParam String email) {
        try {
            if (memberService.verifyEmail(email, token)) {
                // 이메일 인증 성공 시 리다이렉트
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", "/user/join-complete");
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"만료된 토큰입니다.\"}");
            }
        } catch (Exception e) {
            log.error("Error verifying email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"이메일 인증 중 오류가 발생하였습니다. 다시 시도해 주세요.\"}");
        }
    }

    @PostMapping("/user/login")
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

    @PatchMapping("/user/mypage")
    public ResponseEntity<?> updateMember(Principal principal,
                                          @RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto memberResponseDto = memberService.updateMember(principal.getName(), memberRequestDto);
            return ResponseEntity.ok().body(memberResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"회원 정보 수정에 실패하였습니다. 다시 시도해 주세요.\"}");
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
