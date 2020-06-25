package br.com.mercadolivre.proxy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCredential {

    EXCHANGE("exchange-proxy"), ROUTIN_KEY("direct-queue-proxy"), QUEUE("queue-proxy");

    private String value;
}
