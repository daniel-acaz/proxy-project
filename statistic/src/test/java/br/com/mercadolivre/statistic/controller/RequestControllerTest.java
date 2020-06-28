package br.com.mercadolivre.statistic.controller;

import br.com.mercadolivre.statistic.dto.MostFoundDto;
import br.com.mercadolivre.statistic.dto.MostFoundTypeDto;
import br.com.mercadolivre.statistic.dto.RequestDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.service.RequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

    @InjectMocks
    private RequestController controller;

    @Mock
    private RequestService service;

    @Test
    public void shouldGetAllRequestsWithStatus200() {

        when(service.findAll()).thenReturn(buildRequests());

        ResponseEntity<?> response = controller.getAllRequests();

        assertThat(response, hasProperty("statusCode", is(OK)));
        assertThat(response, hasProperty("body", hasSize(10)));

    }

    private List<RequestMessage> buildRequests() {

        List<RequestMessage> requests = new ArrayList<>();

        for(int i = 1; i <= 10; i++) {

            RequestMessage request = RequestMessage.builder()
                    .id((long) i)
                    .allowed(true)
                    .targetPath("targetPath")
                    .originIp("originIp")
                    .requestTime(LocalDateTime.now())
                    .messageId("messageId")
                    .build();

            requests.add(request);
        }

        return requests;
    }

    @Test
    public void shouldGetMaxAmountFoundFromRequestsWithStatus200() {

        RequestDto dto = RequestDto.builder()
                .targetPath(new MostFoundTypeDto())
                .originIp(new MostFoundTypeDto())
                .both(new MostFoundDto())
                .build();

        when(service.getStatisticAboutMostAmount()).thenReturn(dto);

        ResponseEntity<?> response = controller.getMostAmountFoundStatistic();

        assertThat(response, hasProperty("statusCode", is(OK)));
        assertThat(response, hasProperty("body", is(dto)));

    }

}
