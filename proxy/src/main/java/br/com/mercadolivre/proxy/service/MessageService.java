package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.mercadolivre.proxy.constant.MessageCredential.EXCHANGE;
import static br.com.mercadolivre.proxy.constant.MessageCredential.ROUTIN_KEY;

@Service
@Slf4j
public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendRequest(RequestEntity request, boolean allowed) {

        RequestMessage message = RequestMessage.builder()
                .allowed(allowed)
                .id(request.getId())
                .originIp(request.getOriginIp())
                .targetPath(request.getTargetPath())
                .build();

        log.info("sending message: {}", message);
        rabbitTemplate.convertAndSend(EXCHANGE.getValue(), ROUTIN_KEY.getValue(), message);

    }
}
