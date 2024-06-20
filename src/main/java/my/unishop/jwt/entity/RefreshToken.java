package my.unishop.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.unishop.admin.BaseEntity;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter @Setter
@Entity
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;

    private String token;

    public RefreshToken(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
