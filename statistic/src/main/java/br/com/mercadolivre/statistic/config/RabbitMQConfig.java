package br.com.mercadolivre.statistic.config;

import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.repository.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Configuration
@Slf4j
public class RabbitMQConfig {

    @Autowired
    private RequestRepository repository;

    @RabbitListener(queues = "queue-proxy")
    public void receiveMessageFromTopic(Message message) {

        this.saveRequestByMessage(message);

    }

    public Optional<RequestMessage> saveRequestByMessage(Message message) {

        String requestMessage = new String(message.getBody());

        try {
            RequestMessage request = convertMessageAsObject(requestMessage);
            log.info("BODY MESSAGE: {}", requestMessage);
            return Optional.of(repository.save(request));
        } catch (MessageConversionException e) {
            log.info("Cannot convert message: {}", requestMessage, e.getMessage());
        }

        return Optional.empty();

    }

    private RequestMessage convertMessageAsObject(String requestMessage) {
        try {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
                    .readValue(requestMessage, RequestMessage.class);
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable("queue-proxy")
                .build();
    }

}
