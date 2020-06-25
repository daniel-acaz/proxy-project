package br.com.mercadolivre.meliproxy.repository;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RedisTemplateRepository {

    private final RedisTemplate<String, RequestEntity> template;

    @Autowired
    public RedisTemplateRepository(RedisTemplate<String, RequestEntity> template) {
        this.template = template;
    }

    public Set<RequestEntity> findAllByOriginIp(String originIp) {
        String pattern = "*" + originIp + "*";
        Set<String> keys = Objects.requireNonNull(this.template.keys(pattern)).stream()
                .filter(k -> !k.contains("phantom"))
                .collect(Collectors.toSet());

        return keys.parallelStream().map(
                k -> this.template.opsForHash().entries(k)
        ).collect(Collectors.toSet())
                .parallelStream()
                .map(this::convertValue)
                .collect(Collectors.toSet());

    }

    private RequestEntity convertValue(Map<Object, Object> map) {
        return RequestEntity.builder()
                .id(map.get("id").toString())
                .originIp(map.get("originIp").toString())
                .targetPath(map.get("targetPath").toString())
                .requestTime(LocalDateTime.parse(map.get("requestTime").toString()))
                .build();
    }
}
