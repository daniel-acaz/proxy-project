package br.com.mercadolivre.statistic.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDto {

    private MostFoundDto both;
    private MostFoundTypeDto originIp;
    private MostFoundTypeDto targetPath;

}
