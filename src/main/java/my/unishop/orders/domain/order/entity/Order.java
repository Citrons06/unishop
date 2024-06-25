package my.unishop.orders.domain.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.admin.BaseEntity;
import my.unishop.user.domain.member.entity.Address;
import my.unishop.user.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private LocalDateTime orderDate = LocalDateTime.now();

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member user;

    private String order_username;
    private Address orderAddress;
    private String order_tel;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime returnRequestDate;

    @Builder
    public Order(Member user, String order_username, Address orderAddress, String order_tel, OrderStatus orderStatus) {
        this.user = user;
        this.order_username = order_username;
        this.orderAddress = orderAddress;
        this.order_tel = order_tel;
        this.orderStatus = orderStatus;
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCEL;
    }

    public void returnOrder() {
        if (orderStatus != OrderStatus.DELIVERY_COMPLETE) {
            throw new IllegalStateException("배송 완료된 상품만 반품이 가능합니다.");
        }

        if (LocalDateTime.now().isAfter(orderDate.plusDays(3))) {
            throw new IllegalStateException("배송 완료 후 1일 이내에만 반품이 가능합니다.");
        }

        this.orderStatus = OrderStatus.RETURN_REQUESTED;
        this.returnRequestDate = LocalDateTime.now();
    }

    public void completeReturn() {
        if (orderStatus != OrderStatus.RETURN_REQUESTED) {
            throw new IllegalStateException("반품 요청 상태인 주문만 반품 완료 처리가 가능합니다.");
        }

        this.orderStatus = OrderStatus.RETURN_COMPLETE;
    }

    public void completeDelivery() {
        this.orderStatus = OrderStatus.DELIVERY_COMPLETE;
    }
}
