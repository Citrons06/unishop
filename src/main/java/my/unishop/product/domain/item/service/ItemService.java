package my.unishop.product.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.domain.item.entity.Item;
import my.unishop.product.domain.item.repository.ItemRepository;
import my.unishop.product.domain.item.dto.ItemResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //상품 리스트 조회
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItems() {
        List<Item> items = itemRepository.findAll();
        return ItemResponseDto.listFromItems(items);
    }

    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findItemAndItemImgById(itemId);
        return new ItemResponseDto(item);
    }
}
