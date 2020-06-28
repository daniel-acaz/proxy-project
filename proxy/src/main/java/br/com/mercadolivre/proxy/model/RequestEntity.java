package br.com.mercadolivre.proxy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@RedisHash("request")
@Data
@Builder
@AllArgsConstructor
public class RequestEntity {

    private String id;
    private LocalDateTime requestTime;
    private String originIp;
    private String targetPath;

    @TimeToLive long expirationTime;

    public void setDefaultId() {
        this.id = this.originIp + "|" + this.targetPath + "|" + this.requestTime;
    }

}
