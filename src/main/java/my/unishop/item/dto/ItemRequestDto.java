package my.unishop.item.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.item.entity.ItemSellStatus;

@Getter @Setter
@NoArgsConstructor
public class ItemRequestDto {

    @NotNull
    private String itemName;

    @NotNull
    private Integer price;

    @NotNull
    private Integer quantity;

    private Integer item_sell_count;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public ItemRequestDto(String itemName, Integer price, Integer quantity, Integer item_sell_count, ItemSellStatus itemSellStatus) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.item_sell_count = item_sell_count;
        this.itemSellStatus = itemSellStatus;
    }
}
