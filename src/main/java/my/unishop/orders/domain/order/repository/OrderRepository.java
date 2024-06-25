package my.unishop.orders.domain.order.repository;

import my.unishop.orders.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
