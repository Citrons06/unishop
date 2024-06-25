package my.unishop.orders.domain.order.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.product.domain.item.entity.ItemSellStatus;

@Getter @Setter
public class OrderResponseDto {

    private Long orderId;
    private Long itemId;
    private String itemName;
    private int price;
    private int quantity;
    private ItemSellStatus itemSellStatus;
    private int stockQuantity;
}
