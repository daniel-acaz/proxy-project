package br.com.mercadolivre.proxy.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("parameter")
@Builder
public class RequestParameter {

    private String id;
    private Integer limitOrigin;
    private Integer limitTarget;
    private Integer limitBoth;
    private Long expirationTime;

}
