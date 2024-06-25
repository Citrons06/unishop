package my.unishop.orders.domain.order.entity;

public enum OrderStatus {
    // 주문 완료, 주문 확정, 주문 취소, 배송 중, 배송 완료
    ORDERED, ORDER_COMPLETE, CANCELLED, DELIVERING, DELIVERED,

    // 반품 접수, 반품 확정
    RETURN_REQUESTED, RETURN_COMPLETE
}
