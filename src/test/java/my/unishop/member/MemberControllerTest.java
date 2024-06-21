package my.unishop.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.unishop.email.service.VerifyMailRequest;
import my.unishop.member.controller.MemberApiController;
import my.unishop.member.dto.MemberRequestDto;
import my.unishop.member.dto.MemberResponseDto;
import my.unishop.member.dto.RegistrationAttempt;
import my.unishop.member.entity.Member;
import my.unishop.member.entity.UserRole;
import my.unishop.member.repository.MemberRepository;
import my.unishop.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MemberApiController memberController;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        MemberRequestDto memberRequestDto =
                new MemberRequestDto("test", "1234", "010-1234-5678", "abc@naver.com", "Seoul", "1234", "1234", UserRole.USER);

        ResponseEntity<?> response = memberController.signup(memberRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    @DisplayName("이메일 인증 테스트")
    void verifyEmailWithValidCode() throws Exception {
        // 세션 및 요청 설정
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 세션에 필요한 객체 저장
        RegistrationAttempt attempt = new RegistrationAttempt();
        String validCode = "valid_code";
        attempt.setVerificationCode(validCode);

        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setMemberEmail("abc@naver.com");
        memberRequestDto.setPassword("password");
        attempt.setMemberRequestDto(memberRequestDto);

        session.setAttribute("registrationAttempt", attempt);

        // 요청 본문 설정
        VerifyMailRequest verifyMailRequest = new VerifyMailRequest("abc@naver.com", validCode);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(verifyMailRequest);

        // 서비스 모의 설정 및 요청 실행
        mockMvc.perform(MockMvcRequestBuilders.post("/api/verify-email")
                        .session(session) // 요청에 세션 포함
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.memberEmail").value("abc@naver.com"));
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