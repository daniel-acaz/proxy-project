package br.com.mercadolivre.proxy.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("parameter")
@Builder
public class RequestParameter {

    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(example = "10")
    private Integer limitOrigin;

    @ApiModelProperty(example = "10")
    private Integer limitTarget;

    @ApiModelProperty(example = "10")
    private Integer limitBoth;

    @ApiModelProperty(example = "300")
    private Long expirationTime;

}
