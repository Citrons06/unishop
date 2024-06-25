package my.unishop.orders.domain.order.repository;

import my.unishop.orders.domain.order.entity.Order;
import my.unishop.orders.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndReturnRequestDateBefore(OrderStatus orderStatus, LocalDateTime localDateTime);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
