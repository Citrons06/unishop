package my.unishop.global.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.user.domain.member.dto.MemberRequestDto;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class RegistrationAttempt {
    private MemberRequestDto memberRequestDto;
    private String verificationCode;
    private LocalDateTime createdDate;

    public RegistrationAttempt(MemberRequestDto memberRequestDto, String code, LocalDateTime createdDate) {
        this.memberRequestDto = memberRequestDto;
        this.verificationCode = code;
        this.createdDate = createdDate;
    }
}
