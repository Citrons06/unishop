package my.unishop.orders.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequestDto {

    private Long itemId;
    private String itemName;
    private String city;
    private String street;
    private String zipcode;
    private String order_tel;
    private String order_username;
    private int quantity;
    private int orderPrice;

    public OrderRequestDto(Long itemId, String itemName, int price, int quantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.orderPrice = price;
        this.quantity = quantity;
    }
}
