package br.com.mercadolivre.proxy.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class RequestMessage implements Serializable {

    private String id;
    private LocalDateTime requestTime;
    private String originIp;
    private String targetPath;
    private Boolean allowed;
}
