package my.unishop.member.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.email.service.EmailService;
import my.unishop.member.dto.MemberResponseDto;
import my.unishop.member.repository.MemberRepository;
import my.unishop.member.dto.RegistrationAttempt;
import my.unishop.member.dto.MemberRequestDto;
import my.unishop.member.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final HttpSession httpSession;

    //회원 가입 시도
    private void saveRegistrationAttempt(MemberRequestDto memberRequestDto, String verificationCode) {
        RegistrationAttempt attempt = new RegistrationAttempt(memberRequestDto, verificationCode, LocalDateTime.now());
        httpSession.setAttribute("registrationAttempt", attempt);
        log.info("회원 가입 시도: " + memberRequestDto.getUsername() + " / " + memberRequestDto.getMemberEmail() + " / " + verificationCode);
    }

    public void signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }

        if (memberRepository.existsByMemberEmail(memberRequestDto.getMemberEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        String verificationCode = emailService.generateVerificationCode();
        saveRegistrationAttempt(memberRequestDto, verificationCode);

        emailService.sendMail(memberRequestDto.getMemberEmail(), verificationCode);
        log.info("회원 가입 이메일 전송: " + memberRequestDto.getMemberEmail());
        log.info("세션 정보: " + httpSession.getAttribute("registrationAttempt"));
        log.info("verificationCode: " + verificationCode);
    }

    //이메일 인증 및 사용자 정보 저장
    public MemberResponseDto verifyEmail(String email, String code) {
        RegistrationAttempt attempt = (RegistrationAttempt) httpSession.getAttribute("registrationAttempt");
        log.info("이메일 인증 시도: " + email + " / " + code);
        log.info("세션 정보: " + attempt);

        if (attempt != null && attempt.getVerificationCode().equals(code)) {
            Member member = new Member(attempt.getMemberRequestDto());

            if (member.getMemberEmail().equals(email)) {
                memberRepository.save(member);
                httpSession.removeAttribute("registrationAttempt");
                log.info("이메일 인증에 성공하였습니다. 사용자 정보가 저장되었습니다.");

                return new MemberResponseDto(member);
            } else {
                throw new IllegalArgumentException("잘못된 접근입니다.");
            }
        } else {
            throw new IllegalStateException("세션 정보가 만료되었습니다.");
        }
    }

    //회원 정보 수정
    public MemberResponseDto updateMember(String username, MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByUsername(username);
        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        member.updateMember(memberRequestDto);

        return new MemberResponseDto(member);
    }
}