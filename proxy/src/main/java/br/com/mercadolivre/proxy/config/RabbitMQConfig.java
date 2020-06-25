package br.com.mercadolivre.proxy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable("queue-proxy")
                .build();
    }

    @Bean
    public Exchange directExchange() {
        return ExchangeBuilder
                .directExchange("exchange-proxy")
                .durable(true)
                .build();
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder
                .bind(this.queue())
                .to(directExchange())
                .with("direct-queue-proxy")
                .noargs();
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
