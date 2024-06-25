package my.unishop.orders.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.orders.domain.cart.entity.Cart;

@Getter @Setter
@NoArgsConstructor
public class CartResponseDto {

    private Long id;
    private Integer totalPrice;
    private Integer itemCount;
}
