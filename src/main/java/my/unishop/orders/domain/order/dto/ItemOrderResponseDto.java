package my.unishop.orders.domain.order.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.user.domain.member.entity.Address;

@Getter @Setter
public class ItemOrderResponseDto {

    private Long id;
    private String username;
    private Address address;
}
