package my.unishop.product.domain.item.service;

import my.unishop.product.domain.item.dto.ItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ItemService {
    Page<ItemResponseDto> getItems(PageRequest pageRequest);

    Page<ItemResponseDto> searchItemsByName(String itemName, PageRequest pageRequest);

    Page<ItemResponseDto> getItemsByCategory(Long categoryId, PageRequest pageRequest);

    Page<ItemResponseDto> searchItemsByCategoryAndItemName(Long categoryId, String itemName, PageRequest pageRequest);

    ItemResponseDto getItem(Long id);

    void updateItemStock(Long id, int count);

    void updateItemSellCount(Long id, int price);
}
