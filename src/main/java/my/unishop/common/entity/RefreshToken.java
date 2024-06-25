package my.unishop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.admin.BaseRedisEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash("refreshtoken")
public class RefreshToken extends BaseRedisEntity {

    @Id
    private Long id;

    @Indexed
    private String username;

    private String token;

    public RefreshToken(String username, String token) {
        this.username = username;
        this.token = token;
        prePersist();
    }
}
