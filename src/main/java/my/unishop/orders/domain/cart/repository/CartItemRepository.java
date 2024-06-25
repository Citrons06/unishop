package my.unishop.orders.domain.cart.repository;

import my.unishop.orders.domain.cart.entity.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    Optional<CartItem> findById(String cartItemId);
}
