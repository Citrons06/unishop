package my.unishop.product.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.repository.ItemRepository;
import my.unishop.product.domain.item.dto.ItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Page<ItemResponseDto> getItems(PageRequest pageRequest) {
        Page<Item> items = itemRepository.findAll(pageRequest);
        return items.map(ItemResponseDto::new);
    }

    @Override
    public Page<ItemResponseDto> searchItemsByName(String itemName, PageRequest pageRequest) {
        Page<Item> items = itemRepository.findByItemNameContaining(itemName, pageRequest);
        return items.map(ItemResponseDto::new);
    }

    @Override
    public Page<ItemResponseDto> getItemsByCategory(Long categoryId, PageRequest pageRequest) {
        Page<Item> items = itemRepository.findByCategoryId(categoryId, pageRequest);
        return items.map(ItemResponseDto::new);
    }

    @Override
    public Page<ItemResponseDto> searchItemsByCategoryAndItemName(Long categoryId, String itemName, PageRequest pageRequest) {
        Page<Item> items = itemRepository.findByCategoryIdAndItemNameContaining(categoryId, itemName, pageRequest);
        return items.map(ItemResponseDto::new);
    }

    @Override
    public ItemResponseDto getItem(Long id) {
        Item item = itemRepository.findItemById(id);
        return new ItemResponseDto(item);
    }

    @Override
    public void updateItemStock(Long id, int quantity) {
        Item item = itemRepository.findItemById(id);
        item.updateStock(quantity);
    }

    @Override
    public void updateItemSellCount(Long id, int quantity) {
        Item item = itemRepository.findItemById(id);
        item.updateItemSellCount(quantity);
    }
}