package my.unishop.orders.domain.cart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.admin.BaseRedisEntity;
import my.unishop.product.domain.item.entity.ItemImg;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash("cartItem")
@NoArgsConstructor
public class CartItem extends BaseRedisEntity {

    @Id
    private String id;
    private Long itemId;

    @Setter
    private Integer quantity;

    @Setter
    private Integer price;

    @Setter
    private String itemName;

    @Setter
    private List<ItemImg> itemImgList;

    public int getTotalPrice() {
        return price * quantity;
    }

    public CartItem(String id, Long itemId, Integer quantity, Integer price) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }
}