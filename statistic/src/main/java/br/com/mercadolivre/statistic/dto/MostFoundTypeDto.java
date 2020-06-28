package br.com.mercadolivre.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MostFoundTypeDto {

    private String type;

    private Long amount;

}
