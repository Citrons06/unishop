package my.unishop.product.domain.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.domain.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @GetMapping("/item/list")
    public ResponseEntity<?> getItems() {
        return ResponseEntity.ok().body(itemService.getItems());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        return ResponseEntity.ok().body(itemService.getItem(itemId));
    }
}