package my.unishop.orders.domain.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

    private Long itemId;
    private Integer price;
    private Integer availableQuantity;
}