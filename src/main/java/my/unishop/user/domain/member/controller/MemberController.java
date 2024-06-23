package my.unishop.user.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.global.jwt.JwtUtil;
import my.unishop.global.jwt.dto.AuthResponse;
import my.unishop.global.jwt.service.BlackListTokenService;
import my.unishop.user.domain.member.dto.LoginRequestDto;
import my.unishop.user.domain.member.dto.MemberRequestDto;
import my.unishop.user.domain.member.service.AuthService;
import my.unishop.user.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final BlackListTokenService blackListTokenService;
    private final JwtUtil jwtUtil;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    // 회원가입 중 이메일 인증 대기
    @GetMapping("/verify-email")
    public String verifyEmail() {
        return "member/verify-email";
    }

    // 회원가입 완료 페이지
    @GetMapping("/signup-complete")
    public String signupComplete() {
        return "member/signup-complete";
    }

    // 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage() {
        return "member/login";
    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(MemberRequestDto memberRequestDto) {
        try {
            memberService.signup(memberRequestDto);
            return "redirect:/user/verify-email";
        } catch (Exception e) {
            log.error("Signup failed", e);
            return "redirect:/user/signup?error";
        }
    }

    @PostMapping("/login-page")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            AuthResponse authResponse = authService.authenticateUser(loginRequestDto.getUsername(), loginRequestDto.getPassword());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // 로그아웃
    @PostMapping("/logout")
    public String logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        log.info("Received refresh token: {}", refreshToken);

        try {
            authService.logout(refreshToken);
            blackListTokenService.blacklistToken(refreshToken);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            log.error("Invalid refresh token", e);
            return "redirect:/user/login-page?error";
        }
    }
}
