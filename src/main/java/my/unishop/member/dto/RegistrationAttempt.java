package my.unishop.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
