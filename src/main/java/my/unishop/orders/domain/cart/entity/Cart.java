package my.unishop.orders.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.admin.BaseEntity;
import my.unishop.user.domain.member.entity.Member;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    private Integer totalPrice;
    private Integer itemCount;

    @OneToOne(fetch = FetchType.LAZY)
    private Member user;
}
