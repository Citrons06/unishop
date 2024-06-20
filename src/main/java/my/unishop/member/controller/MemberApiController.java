package my.unishop.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.email.service.VerifyMailRequest;
import my.unishop.member.dto.MemberResponseDto;
import my.unishop.member.service.MemberService;
import my.unishop.member.dto.MemberRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        userService.signup(memberRequestDto);
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body("{\"msg\" : \"인증 메일이 전송되었습니다.\"}");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyMailRequest request) {
        try {
            log.info("UserController.verifyEmail(): " + request.getEmail() + " / " + request.getCode());
            MemberResponseDto memberResponseDto = userService.verifyEmail(request.getEmail(), request.getCode());

            return ResponseEntity.ok().body(memberResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"인증을 실패하였습니다. 다시 시도해 주세요.\"}");
        }
    }

    @PutMapping("/user/mypage")
    public ResponseEntity<?> updateMember(Principal principal,
                                          @RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto memberResponseDto = userService.updateMember(principal.getName(), memberRequestDto);
            return ResponseEntity.ok().body(memberResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"회원 정보 수정에 실패하였습니다. 다시 시도해 주세요.\"}");
        }
    }
}
