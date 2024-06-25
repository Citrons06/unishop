package my.unishop.product.domain.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.admin.BaseEntity;
import my.unishop.product.domain.item.dto.ItemRequestDto;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static my.unishop.product.domain.item.entity.ItemSellStatus.SOLD_OUT;

@Getter
@Entity
@NoArgsConstructor
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private Integer price;

    private Integer quantity;

    private Integer itemSellCount = 0;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemImg> itemImgList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Item(ItemRequestDto itemRequestDto, Category category) {
        this.itemName = itemRequestDto.getItemName();
        this.price = itemRequestDto.getPrice();
        this.quantity = itemRequestDto.getQuantity();
        this.itemSellStatus = itemRequestDto.getItemSellStatus();
        this.category = category;
        this.itemSellCount = 0;
    }

    public void updateItem(ItemRequestDto itemRequestDto, Category category) {
        this.itemName = itemRequestDto.getItemName();
        this.price = itemRequestDto.getPrice();
        this.quantity = itemRequestDto.getQuantity();
        this.itemSellStatus = itemRequestDto.getItemSellStatus();
        this.category = category;
    }

    public void updateItemImgs(List<ItemImg> newItemImgs) {
        this.itemImgList.clear();
        this.itemImgList.addAll(newItemImgs);
    }

    public void decreaseStock(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }
        this.quantity -= quantity;

        if (this.quantity == 0) {
            this.itemSellStatus = SOLD_OUT;
        }
    }

    public void updateStock(int quantity) {
        this.quantity = quantity;
    }

    public void updateItemSellCount(int quantity) {
        if (this.itemSellCount == null) {
            this.itemSellCount = 0;
        }
        this.itemSellCount += quantity;
    }
}