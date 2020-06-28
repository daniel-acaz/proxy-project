package br.com.mercadolivre.statistic.config;

import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.repository.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RabbitMQConfigTest {

    @InjectMocks
    private RabbitMQConfig config;

    @Mock
    private RequestRepository repository;


    @Test
    public void shouldSaveMessageWithSuccess() throws JsonProcessingException {

        RequestMessage requestMessage = RequestMessage.builder()
                .id(1L)
                .requestTime(LocalDateTime.now())
                .messageId("Message Id")
                .allowed(true)
                .targetPath("Target Path")
                .originIp("Origin Ip")
                .build();

        when(repository.save(any())).then(invocationOnMock -> invocationOnMock.getArgument(0));

        String messageBody = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(requestMessage);

        Message message = mock(Message.class);

        when(message.getBody()).thenReturn(messageBody.getBytes());

        Optional<RequestMessage> optionalRequestMessage = config.saveRequestByMessage(message);

        assertTrue(optionalRequestMessage.isPresent());
        assertThat(optionalRequestMessage.get(), hasProperty("id", is(1L)));

    }

    @Test
    public void shouldNotSaveMessageWithSuccess() {

        String messageBody = "Message Body";

        Message message = mock(Message.class);

        when(message.getBody()).thenReturn(messageBody.getBytes());

        Optional<RequestMessage> optionalRequestMessage = config.saveRequestByMessage(message);

        assertFalse(optionalRequestMessage.isPresent());

    }

}
