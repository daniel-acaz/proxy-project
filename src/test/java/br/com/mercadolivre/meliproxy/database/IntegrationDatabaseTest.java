package br.com.mercadolivre.meliproxy.database;

import br.com.mercadolivre.meliproxy.MeliProxyApplication;
import br.com.mercadolivre.meliproxy.model.RequestEntity;
import br.com.mercadolivre.meliproxy.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = MeliProxyApplication.class)
public class IntegrationDatabaseTest {

    @Autowired
    private RequestRepository repository;

    @Test
    public void shouldSaveARequestAndGetAll() {

        repository.saveAll(buildRequestMocks());

        int countByOriginIp = repository
                .countByOriginIpAndRequestTimeAfter("0.0.0.0:0000",
                        LocalDateTime.now().minus(5, ChronoUnit.MINUTES));

        int countByTargetPath = repository
                .countByTargetPathAndRequestTimeAfter("api/sites/MLB/categories/1",
                        LocalDateTime.now().minus(5, ChronoUnit.MINUTES));

        int countByBoth = repository
                .countByOriginIpAndTargetPathAndRequestTimeAfter("0.0.0.0:0002",
                        "api/sites/MLB/categories/2",
                        LocalDateTime.now().minus(5, ChronoUnit.MINUTES));

        assertEquals(countByOriginIp, 1);
        assertEquals(countByTargetPath, 1);
        assertEquals(countByBoth, 1);

    }

    private List<RequestEntity> buildRequestMocks() {

        List<RequestEntity> requests = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            RequestEntity request = RequestEntity.builder()
                    .id((long) i)
                    .requestTime(LocalDateTime.now().minus(15, ChronoUnit.MINUTES))
                    .targetPath("api/sites/MLB/categories/" + i)
                    .originIp("0.0.0.0:000" + i)
                    .build();
            requests.add(request);
        }

        for (int i = 3; i < 7; i++) {
            RequestEntity request = RequestEntity.builder()
                    .id((long) i)
                    .requestTime(LocalDateTime.now())
                    .targetPath("api/sites/MLB/categories/" + (i - 3))
                    .originIp("0.0.0.0:000" + (i - 3))
                    .build();
            requests.add(request);
        }

        return requests;

    }

}
