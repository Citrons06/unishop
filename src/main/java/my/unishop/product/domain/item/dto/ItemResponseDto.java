package my.unishop.product.domain.item.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import my.unishop.product.domain.item.entity.ItemImg;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.entity.ItemSellStatus;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemResponseDto {

    private Long id;

    private String itemName;

    private Integer price;

    private Integer quantity;

    private Integer item_sell_count;

    private List<ItemImg> itemImgList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private Long categoryId;

    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.item_sell_count = item.getItem_sell_count();
        this.itemSellStatus = item.getItemSellStatus();
        this.categoryId = item.getCategory().getId();
        this.itemImgList = item.getItemImgList();
    }

    public static List<ItemResponseDto> listFromItems(List<Item> items) {
        return items.stream().map(ItemResponseDto::new).toList();
    }
}
