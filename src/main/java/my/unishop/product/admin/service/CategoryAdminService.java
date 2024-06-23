package my.unishop.product.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.unishop.product.admin.repository.CategoryRepository;
import my.unishop.product.domain.item.dto.CategoryRequestDto;
import my.unishop.product.domain.item.dto.CategoryResponseDto;
import my.unishop.product.domain.item.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto updateCategory(Long categoryId, CategoryResponseDto categoryResponseDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        category.update(categoryResponseDto);

        log.info("카테고리 업데이트 : " + categoryResponseDto.getCategoryName());
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryResponseDto::new).collect(Collectors.toList());
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.save(categoryRequestDto.toEntity());

        log.info("카테고리 생성 : " + categoryRequestDto.getCategoryName());
        return new CategoryResponseDto(category);
    }

    public void deleteCategory(Long categoryId) {
        log.info("카테고리 삭제 : " + categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
