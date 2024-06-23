package my.unishop.product.domain.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.admin.service.CategoryAdminService;
import my.unishop.product.admin.service.ItemAdminService;
import my.unishop.product.domain.item.dto.CategoryResponseDto;
import my.unishop.product.domain.item.dto.ItemResponseDto;
import my.unishop.product.domain.item.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemAdminService itemAdminService;
    private final CategoryAdminService categoryAdminService;

    @GetMapping("/item/list")
    public String itemList(@RequestParam(value = "search", required = false) String search,
                           @RequestParam(value = "category", required = false) Long categoryId,
                           Model model) {

        List<ItemResponseDto> items;
        List<CategoryResponseDto> categories = categoryAdminService.getCategories();
        if (search != null && !search.isEmpty()) {
            if (categoryId != null) {
                items = itemAdminService.searchItemsByCategoryAndItemName(categoryId, search);
            } else {
                items = itemAdminService.searchItemsByName(search);
            }
        } else {
            if (categoryId != null) {
                items = itemAdminService.getItemsByCategory(categoryId);
            } else {
                items = itemAdminService.getItems();
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        return "items/itemList";
    }

    @GetMapping("/item/{itemId}")
    public String getItem(Model model, @PathVariable Long itemId) {
        ItemResponseDto item = itemService.getItem(itemId);
        model.addAttribute("item", item);
        return "items/itemDetail";
    }
}
