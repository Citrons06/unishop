package my.unishop.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.ItemImg.entity.ItemImg;
import my.unishop.admin.BaseEntity;
import my.unishop.cart.entity.CartItem;
import my.unishop.category.entity.Category;
import my.unishop.item.dto.ItemRequestDto;

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

    private Integer item_sell_count;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemImg> itemImgs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Item(ItemRequestDto itemRequestDto) {
        this.itemName = itemRequestDto.getItemName();
        this.price = itemRequestDto.getPrice();
        this.quantity = itemRequestDto.getQuantity();
        this.itemSellStatus = itemRequestDto.getItemSellStatus();
    }
}
