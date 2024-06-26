package my.unishop.orders.domain.cart.repository;

import my.unishop.orders.domain.cart.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {
}