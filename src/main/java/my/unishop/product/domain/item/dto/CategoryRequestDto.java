package my.unishop.product.domain.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.product.domain.item.entity.Category;

@Getter @Setter
@NoArgsConstructor
public class CategoryRequestDto {

    private String categoryName;

    public CategoryRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category toEntity() {
        return new Category(this);
    }
}
