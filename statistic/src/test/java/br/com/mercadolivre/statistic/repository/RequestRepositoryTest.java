package br.com.mercadolivre.statistic.repository;

import br.com.mercadolivre.statistic.StatisticApplication;
import br.com.mercadolivre.statistic.model.RequestMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(StatisticApplication.class)
public class RequestRepositoryTest {

    @Autowired
    private RequestRepository repository;

    @Test
    public void shouldGetCountByTargetPathAndOriginIp() {

        repository.saveAll(buildMockRequests());
        List<Object[]> countByTargetPath = repository.getCountByTargetPathAndOriginIp();

        assertThat(countByTargetPath, hasSize(2));
        assertThat(countByTargetPath.get(0)[2], is("SAME_ORIGIN_IP"));
        assertThat(countByTargetPath.get(0)[1], is("SAME_TARGET_PATH"));
        assertThat(countByTargetPath.get(0)[0], is(10L));

    }

    private List<RequestMessage> buildMockRequests() {

        List<RequestMessage> requests = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            RequestMessage request = RequestMessage.builder()
                    .allowed(true)
                    .id((long) i)
                    .messageId("message: " + i)
                    .originIp("SAME_ORIGIN_IP")
                    .requestTime(LocalDateTime.now())
                    .targetPath("SAME_TARGET_PATH")
                    .build();
            requests.add(request);
        }

        RequestMessage request = RequestMessage.builder()
                .allowed(true)
                .id(11L)
                .messageId("message: " + 11)
                .originIp("OTHER_ORIGIN_IP")
                .requestTime(LocalDateTime.now())
                .targetPath("OTHER_TARGET_PATH")
                .build();
        requests.add(request);

        return requests;
    }

}
