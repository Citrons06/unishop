package my.unishop.product.domain.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ItemImg {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName;

    private String imgUrl;
    private String oriImgName;
    private String repImgYn;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemImg(MultipartFile itemImgFile) {
        this.imgName = itemImgFile.getOriginalFilename();
        this.oriImgName = itemImgFile.getOriginalFilename();
    }
}

