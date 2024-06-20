package my.unishop.member;

import my.unishop.email.service.VerifyMailRequest;
import my.unishop.member.controller.MemberApiController;
import my.unishop.member.dto.MemberRequestDto;
import my.unishop.member.entity.UserRole;
import my.unishop.member.repository.MemberRepository;
import my.unishop.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MemberApiController memberController;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        MemberRequestDto memberRequestDto =
                new MemberRequestDto("test", "1234", "010-1234-5678", "abc@naver.com", "Seoul", "1234", "1234", UserRole.USER);

        ResponseEntity<?> response = memberController.signup(memberRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("이메일 인증 테스트")
    void verifyEmail() {
        VerifyMailRequest request = new VerifyMailRequest("abc@naver.com", "invalid_code");

        ResponseEntity<?> response = memberController.verifyEmail(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateMember() {
        MemberRequestDto memberRequestDto =
                new MemberRequestDto("test", "1234", "010-1234-5678", "abc@naver.com", "Seoul", "1234", "1234", UserRole.USER);

        memberRepository.save(memberRequestDto.toEntity());

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "test";
            }
        };

        memberController.updateMember(principal, new MemberRequestDto("test", "5678", "010-5678-1234", "abc@naver.com", "Busan", "5678", "5678", UserRole.USER));

        assertEquals("Busan", memberRepository.findByUsername("test").getMemberAddress().getCity());
    }
}