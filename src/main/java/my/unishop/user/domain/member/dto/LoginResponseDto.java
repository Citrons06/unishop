package my.unishop.user.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.common.jwt.dto.TokenDto;

@Getter @Setter
public class LoginResponseDto {
    private String username;
    private TokenDto tokenDto;

    public LoginResponseDto(String username, TokenDto tokenDto) {
        this.username = username;
        this.tokenDto = tokenDto;
    }
}
