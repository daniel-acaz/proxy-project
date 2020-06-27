package br.com.mercadolivre.statistic.service;

import br.com.mercadolivre.statistic.dto.MostFoundDto;
import br.com.mercadolivre.statistic.dto.MostFoundTypeDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestFunction {

    public Map<Long, RequestMessage> getAmountMap(List<Object[]> resultSet) {

        return resultSet.stream()
                .collect(Collectors.toMap(count -> (Long) count[0],
                        count -> RequestMessage.builder()
                                .targetPath(count[1].toString())
                                .originIp(count[2].toString())
                                .build()
                ));

    }

    public MostFoundTypeDto getMostFoundTypeByMap(Map<Long, RequestMessage> resultQuery, Set<String> types) {

        return types.stream().collect(
                Collectors.toMap(
                        type -> type,
                        type -> resultQuery.entrySet().stream()
                                .filter(entry -> entry.getValue().getOriginIp().equals(type))
                                .map(Map.Entry::getKey)
                                .reduce(Long::sum).orElse(0L)
                    )
                ).entrySet().stream()
                        .max(Comparator.comparingLong(Map.Entry::getValue))
                        .map(m -> MostFoundTypeDto.builder()
                                .type(m.getKey())
                                .amount(m.getValue())
                                .build()).orElse(new MostFoundTypeDto());
    }

    public MostFoundDto getMostFoundByMap(Map<Long, RequestMessage> resultQuery) {

        return resultQuery.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getKey))
                .map(map -> MostFoundDto.builder()
                        .amount(map.getKey())
                        .targetPah(map.getValue().getTargetPath())
                        .originIp(map.getValue().getOriginIp())
                        .build())
                .orElse(new MostFoundDto());
    }

}
