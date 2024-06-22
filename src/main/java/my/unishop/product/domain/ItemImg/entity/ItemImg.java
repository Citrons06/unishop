package my.unishop.product.domain.ItemImg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.product.domain.item.entity.Item;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
public class ItemImg {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName;

    private String imgUrl;
    private String oriImgName;
    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
