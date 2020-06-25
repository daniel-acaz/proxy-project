package br.com.mercadolivre.proxy.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage {

    private String messageId;
    private LocalDateTime requestTime;
    private String originIp;
    private String targetPath;
    private Boolean allowed;
}
