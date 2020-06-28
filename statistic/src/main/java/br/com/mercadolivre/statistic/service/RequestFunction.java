package br.com.mercadolivre.statistic.service;

import br.com.mercadolivre.statistic.dto.MostFoundDto;
import br.com.mercadolivre.statistic.dto.MostFoundTypeDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestFunction {

    public Map<Long, RequestMessage> getAmountMap(List<Object[]> resultSet) {

        if(resultSet.isEmpty()) return new HashMap<>();

        resultSet.sort(Comparator.comparingLong(object -> (long) object[0]));
        Collections.reverse(resultSet);

        List<Object[]> objects = Collections.singletonList(resultSet.get(0));

        return objects.stream()
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
                                .filter(entry -> type.equals(entry.getValue().getOriginIp()) ||
                                        type.equals(entry.getValue().getTargetPath()))
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
