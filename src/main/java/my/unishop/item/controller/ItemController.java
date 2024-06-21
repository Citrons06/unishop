package my.unishop.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.item.dto.ItemResponseDto;
import my.unishop.item.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item/list")
    public String getItems(Model model) {
        List<ItemResponseDto> items = itemService.getItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/item/{itemid}")
    public String getItem(Model model, @PathVariable Long itemid) {
        ItemResponseDto item = itemService.getItem(itemid);
        model.addAttribute("item", item);
        return "items/itemDetail";
    }
}
