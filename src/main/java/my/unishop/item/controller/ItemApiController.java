package my.unishop.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.item.dto.ItemRequestDto;
import my.unishop.item.dto.ItemResponseDto;
import my.unishop.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/item/create")
    public ResponseEntity<?> createItem(@RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto itemResponseDto = itemService.createItem(itemRequestDto);
        return ResponseEntity.ok().body(itemResponseDto);
    }

    @GetMapping("/item/list")
    public ResponseEntity<?> getItems() {
        return ResponseEntity.ok().body(itemService.getItems());
    }

    @GetMapping("/item/{itemid}")
    public ResponseEntity<?> getItem(@PathVariable Long itemid) {
        return ResponseEntity.ok().body(itemService.getItem(itemid));
    }
}

