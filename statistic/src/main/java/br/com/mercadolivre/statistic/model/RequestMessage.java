package br.com.mercadolivre.statistic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
@Entity
@Table(name = "request")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestMessage implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String messageId;
    private LocalDateTime requestTime;
    private String originIp;
    private String targetPath;
    private Boolean allowed;

}
