package br.com.mercadolivre.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MostFoundDto {

    private String targetPah;
    private String originIp;
    private Long amount;
}
