package my.unishop.user.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.user.domain.member.entity.Address;

@Getter @Setter
public class MemberOrderResponseDto {
    private Long id;
    private String username;
    private Address address;
}
