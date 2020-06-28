package br.com.mercadolivre.proxy.repository;

import br.com.mercadolivre.proxy.model.RequestEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedisTemplateRepositoryTest {

    @InjectMocks
    private RedisTemplateRepository repository;

    @Mock
    private RedisTemplate<String, RequestEntity> template;

    @Test
    public void shouldFindAllKeysByRegexPattern() {

        String mockKey = "key";

        HashOperations hashOperations = mock(HashOperations.class);

        Map<Object, Object> mockMap = new HashMap<>();
        mockMap.put("id", "id");
        mockMap.put("originIp", "originIp");
        mockMap.put("targetPath", "targetPath");
        mockMap.put("requestTime", "1986-04-08T12:30:10.000");

        when(hashOperations.entries(any())).thenReturn(mockMap);
        when(template.opsForHash()).thenReturn(hashOperations);
        when(template.keys("*" + mockKey + "*")).thenReturn(
                new HashSet<>(Arrays.asList("key 1", "key 2", "key 3"))
        );

        Set<RequestEntity> requests = repository.findAllByKeyRegexPattern(mockKey);

        assertThat(requests, hasSize(1));
        assertThat(requests.iterator().next(), hasProperty("id", is("id")));


    }
}
