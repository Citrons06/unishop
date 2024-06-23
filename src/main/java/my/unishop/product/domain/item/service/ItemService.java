package my.unishop.product.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.repository.ItemRepository;
import my.unishop.product.domain.item.dto.ItemResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemResponseDto> getItems() {
        List<Item> items = itemRepository.findAllWithImagesAndCategory();
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }

    public List<ItemResponseDto> searchItemsByName(String name) {
        List<Item> items = itemRepository.findByItemNameContaining(name);
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }

    public List<ItemResponseDto> getItemsByCategory(Long categoryId) {
        List<Item> items = itemRepository.findByCategoryId(categoryId);
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }

    public List<ItemResponseDto> searchItemsByCategoryAndItemName(Long categoryId, String name) {
        List<Item> items = itemRepository.findByCategoryIdAndItemNameContaining(categoryId, name);
        return items.stream().map(ItemResponseDto::new).collect(Collectors.toList());
    }

    public ItemResponseDto getItem(Long id) {
        Item item = itemRepository.findItemAndItemImgById(id);
        return new ItemResponseDto(item);
    }
}
