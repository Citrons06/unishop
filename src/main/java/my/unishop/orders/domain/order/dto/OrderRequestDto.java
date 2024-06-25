package my.unishop.orders.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequestDto {

    private Long itemId;

    private String city;
    private String street;
    private String zipcode;
    private String order_tel;
    private String order_username;
    private int quantity;

}
