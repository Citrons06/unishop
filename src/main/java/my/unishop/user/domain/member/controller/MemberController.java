package my.unishop.user.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.user.domain.member.dto.LoginRequestDto;
import my.unishop.user.domain.member.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    // 회원가입 완료 페이지
    @GetMapping("/signup-complete")
    public String signupComplete() {
        return "signup-complete";
    }

    // 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage() {
        return "member/login";
    }

    @PostMapping("/login-page")
    public String authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            authService.authenticateUser(loginRequestDto.getUsername(), loginRequestDto.getPassword());
            return "redirect:/";
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return "redirect:/user/login-page?error";
        }
    }
}
