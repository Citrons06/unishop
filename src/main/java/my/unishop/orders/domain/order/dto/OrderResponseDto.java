package my.unishop.orders.domain.order.dto;

import lombok.Getter;
import lombok.Setter;
import my.unishop.orders.domain.order.entity.Order;
import my.unishop.orders.domain.order.entity.OrderItem;
import my.unishop.orders.domain.order.entity.OrderStatus;

@Getter @Setter
public class OrderResponseDto {

    private Long orderId;
    private Long itemId;
    private String itemName;
    private int price;
    private int count;
    private OrderStatus orderStatus;
    private int orderPrice;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        if (!order.getOrderItems().isEmpty()) {
            OrderItem orderItem = order.getOrderItems().get(0); // assuming one item per order for simplicity
            this.itemId = orderItem.getItem().getId();
            this.itemName = orderItem.getItem().getItemName();
            this.price = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
            this.orderStatus = order.getOrderStatus();
        }
        this.orderPrice = order.getTotalPrice();
    }
}
