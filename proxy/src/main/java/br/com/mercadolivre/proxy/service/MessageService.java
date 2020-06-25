package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

import static br.com.mercadolivre.proxy.constant.MessageCredential.EXCHANGE;
import static br.com.mercadolivre.proxy.constant.MessageCredential.ROUTING_KEY;

@Service
@Slf4j
public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendRequest(RequestEntity request, boolean allowed) {

        RequestMessage requestMessage = RequestMessage.builder()
                .allowed(allowed)
                .messageId(request.getId())
                .originIp(request.getOriginIp())
                .targetPath(request.getTargetPath())
                .requestTime(request.getRequestTime())
                .build();

        log.info("sending message: {}", requestMessage);
        String message = convertMessageAsString(requestMessage);
        rabbitTemplate.convertAndSend(EXCHANGE.getValue(), ROUTING_KEY.getValue(), message);

    }

    private String convertMessageAsString(RequestMessage requestMessage) {
        try {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .writeValueAsString(requestMessage);
        } catch (JsonProcessingException e) {
            log.info("Cannot converter message id: {}", requestMessage.getMessageId(), e.getMessage());
            return requestMessage.toString();
        }
    }
}
