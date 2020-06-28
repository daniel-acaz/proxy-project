package br.com.mercadolivre.proxy.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@AllArgsConstructor
@Getter
public class RequestMessage {

    private String messageId;
    private LocalDateTime requestTime;
    private String originIp;
    private String targetPath;
    private Boolean allowed;
}
