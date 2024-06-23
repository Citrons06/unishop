package my.unishop.product.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.domain.item.dto.CategoryResponseDto;
import my.unishop.product.admin.service.CategoryAdminService;
import my.unishop.product.admin.service.ItemAdminService;
import my.unishop.product.domain.item.dto.ItemRequestDto;
import my.unishop.product.domain.item.dto.ItemResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ItemAdminController {

    private final ItemAdminService itemAdminService;
    private final CategoryAdminService categoryAdminService;

    @GetMapping("/item/create")
    public String createItemForm(Model model) {
        model.addAttribute(new ItemRequestDto());
        model.addAttribute("categories", categoryAdminService.getCategories());
        return "admin/items/createItemForm";
    }

    @GetMapping("/item/update/{itemId}")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        ItemResponseDto itemResponseDto = itemAdminService.getItem(itemId);
        model.addAttribute("item", itemResponseDto);
        model.addAttribute("categories", categoryAdminService.getCategories());
        return "admin/items/updateItemForm";
    }

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
        return "admin/items/itemList";
    }


    @PostMapping("/item/create")
    public String createItem(@Valid @ModelAttribute("item") ItemRequestDto itemRequestDto,
                             List<MultipartFile> itemImgFileList) {

        try {
            itemRequestDto.setItemImgFileList(itemImgFileList);
            itemAdminService.createItem(itemRequestDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/admin/item/list";
    }

    @PutMapping("/item/update/{itemId}")
    public String updateItem(@PathVariable Long itemId,
                             @Valid @ModelAttribute("item") ItemRequestDto itemRequestDto,
                             List<MultipartFile> itemImgFileList,
                             Long categoryId) {

        try {
            itemAdminService.updateItem(itemId, itemRequestDto, itemImgFileList, categoryId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/admin/item/list";
    }

    @DeleteMapping("/item/delete/{itemId}")
    public String deleteItem(@PathVariable Long itemId) {
        itemAdminService.deleteItem(itemId);
        return "redirect:/admin/item/list";
    }
}
