package my.unishop.user.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class VerifyMailRequest {

    private String email;
    private String code;

    public VerifyMailRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
