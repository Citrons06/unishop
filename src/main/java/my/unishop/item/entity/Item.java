package my.unishop.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.admin.BaseEntity;
import my.unishop.cart.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private Integer price;

    private Integer quantity;

    private Integer item_sell;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();
}
