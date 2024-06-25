package my.unishop.common.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenDto {

    private String username;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenDto(String username, String accessToken, String refreshToken) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
