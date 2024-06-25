package my.unishop.orders.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@Getter
@NoArgsConstructor
@RedisHash("cart")
public class Cart implements Serializable {

    @Id
    @Column(name = "cart_id")
    private Long id;

    private Integer totalPrice;
    private Integer quantity;
    private Map<String, Integer> item;

    public Cart(Long id, Integer totalPrice, Integer quantity, Map<String, Integer> item) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.item = item;
    }
}
