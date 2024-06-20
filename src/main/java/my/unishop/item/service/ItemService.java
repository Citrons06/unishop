package my.unishop.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.item.dto.ItemRequestDto;
import my.unishop.item.dto.ItemResponseDto;
import my.unishop.item.entity.Item;
import my.unishop.item.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //상품 등록
    public ItemResponseDto createItem(ItemRequestDto itemRequestDto) {
        Item item = new Item(itemRequestDto);
        itemRepository.save(item);
        log.info("상품 등록: " + item.getItemName());

        return new ItemResponseDto(item);
    }

    //상품 리스트 조회
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItems() {
        List<Item> items = itemRepository.findAll();
        return ItemResponseDto.listFromItems(items);
    }

    public Object getItem(Long itemid) {
        Item item = itemRepository.findById(itemid)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + itemid));
        return new ItemResponseDto(item);
    }
}
