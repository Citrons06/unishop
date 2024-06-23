package my.unishop.product.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.admin.service.CategoryAdminService;
import my.unishop.product.domain.item.dto.CategoryRequestDto;
import my.unishop.product.domain.item.dto.CategoryResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    @GetMapping("/category/create")
    public String createCategory(Model model) {
        model.addAttribute("category", new CategoryRequestDto());
        return "admin/category/createCategoryForm";
    }

    @GetMapping("/category/update/{categoryId}")
    public String updateCategory(@PathVariable Long categoryId, Model model) {
        CategoryResponseDto categoryResponseDto = categoryAdminService.getCategory(categoryId);
        model.addAttribute("category", categoryResponseDto);
        return "admin/category/updateCategoryForm";
    }

    @GetMapping("/category/list")
    public String categoryList(Model model) {
        model.addAttribute("categories", categoryAdminService.getCategories());
        return "admin/category/categoryList";
    }

    @PostMapping("/category/create")
    public String createCategory(CategoryRequestDto categoryRequestDto) {
        categoryAdminService.createCategory(categoryRequestDto);
        return "redirect:/admin/category/list";
    }

    @PutMapping("/category/update/{categoryId}")
    public String updateCategory(@PathVariable Long categoryId, CategoryResponseDto categoryResponseDto) {
        categoryAdminService.updateCategory(categoryId, categoryResponseDto);
        return "redirect:/admin/category/list";
    }

    @DeleteMapping("/category/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        categoryAdminService.deleteCategory(categoryId);
        return "redirect:/admin/category/list";
    }
}
