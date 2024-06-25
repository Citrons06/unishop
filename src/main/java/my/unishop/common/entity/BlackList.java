package my.unishop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import my.unishop.admin.BaseRedisEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash("blacklist")
public class BlackList extends BaseRedisEntity {

    @Id
    private Long id;

    @Indexed
    private String refreshToken;

    public BlackList(String refreshToken) {
        this.refreshToken = refreshToken;
        prePersist();
    }
}