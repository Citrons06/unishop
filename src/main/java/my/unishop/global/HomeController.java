package my.unishop.global;

import lombok.RequiredArgsConstructor;
import my.unishop.product.admin.service.CategoryAdminService;
import my.unishop.product.domain.item.dto.CategoryResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryAdminService categoryAdminService;

    @GetMapping("/")
    public String home(Model model) {
        List<CategoryResponseDto> categories = categoryAdminService.getCategories();
        model.addAttribute("categories", categories);
        return "index";
    }
}
