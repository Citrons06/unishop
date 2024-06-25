package my.unishop.user.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.common.util.JwtUtil;
import my.unishop.common.jwt.service.BlackListTokenService;
import my.unishop.user.domain.member.dto.MemberRequestDto;
import my.unishop.user.domain.member.dto.MemberResponseDto;
import my.unishop.user.domain.member.service.AuthService;
import my.unishop.user.domain.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            MemberResponseDto member = memberService.findMember(username);

            model.addAttribute("member", member);
        } else {
            // 토큰이 유효하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/user/login-page";
        }

        return "member/mypage";
    }

    @PatchMapping("/mypage")
    public String mypage(@ModelAttribute("member") MemberRequestDto memberRequestDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = jwtUtil.resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            memberService.updateMember(username, memberRequestDto);
            redirectAttributes.addFlashAttribute("successMessage", "정보가 성공적으로 업데이트되었습니다.");

        } else {
            return "redirect:/user/mypage";
        }

        return "member/mypage";
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
