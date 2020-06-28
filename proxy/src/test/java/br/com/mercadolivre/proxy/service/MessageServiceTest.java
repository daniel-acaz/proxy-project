package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService service;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ObjectMapper mapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    public void shouldSendRequestMessageSuccess() {

        String expected = "{\"messageId\":\"id\",\"requestTime\":\"2020-06-26T10:12:00\",\"originIp\":\"0.0.0.0:00\",\"targetPath\":\"/api/sites/MLB\",\"allowed\":true}";

        RequestEntity mockRequest = RequestEntity.builder()
                .id("id")
                .expirationTime(10L)
                .originIp("0.0.0.0:00")
                .requestTime(LocalDateTime.of(2020, 6, 26, 10, 12, 0,0))
                .targetPath("/api/sites/MLB")
                .build();

        mapper = new ObjectMapper();
        service.setMapper(mapper);

        String message = this.service.sendRequest(mockRequest, true);

        assertThat(message, is(expected));
    }

    @Test
    public void shouldNotSendRequestMessageSuccess() throws JsonProcessingException {

        RequestEntity mockRequest = RequestEntity.builder()
                .id("id")
                .expirationTime(10L)
                .originIp("0.0.0.0:00")
                .requestTime(LocalDateTime.of(2020, 6, 26, 10, 12, 0,0))
                .targetPath("/api/sites/MLB")
                .build();

        RequestMessage mockMessage = RequestMessage.builder()
                .allowed(true)
                .messageId(mockRequest.getId())
                .originIp(mockRequest.getOriginIp())
                .targetPath(mockRequest.getTargetPath())
                .requestTime(mockRequest.getRequestTime())
                .build();

        mapper = spy(new ObjectMapper());
        when(mapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        service.setMapper(mapper);

        String message = this.service.sendRequest(mockRequest, true);

        assertThat(message, is(mockMessage.toString()));
    }

}
