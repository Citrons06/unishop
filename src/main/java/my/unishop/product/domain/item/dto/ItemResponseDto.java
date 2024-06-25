package my.unishop.product.domain.item.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.entity.ItemSellStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class ItemResponseDto {

    private Long id;
    private String itemName;
    private int price;
    private int quantity;
    private boolean hasImages;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private List<ItemImgResponseDto> itemImgList = new ArrayList<>();

    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.quantity = (item.getQuantity() != null) ? item.getQuantity() : 0;
        this.itemSellStatus = item.getItemSellStatus();
        this.itemImgList = item.getItemImgList().stream()
                .map(ItemImgResponseDto::new)
                .collect(Collectors.toList());
        this.hasImages = !this.itemImgList.isEmpty();
    }
}
