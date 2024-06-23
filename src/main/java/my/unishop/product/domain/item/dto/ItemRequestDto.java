package my.unishop.product.domain.item.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.product.domain.item.entity.ItemSellStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ItemRequestDto {

    @NotNull
    private String itemName;

    @NotNull
    private Integer price;

    @NotNull
    private Integer quantity;

    private Integer item_sell_count;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private List<MultipartFile> itemImgFileList = new ArrayList<>();

    private Long categoryId;
}
