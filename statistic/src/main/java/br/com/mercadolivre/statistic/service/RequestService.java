package br.com.mercadolivre.statistic.service;

import br.com.mercadolivre.statistic.dto.MostFoundDto;
import br.com.mercadolivre.statistic.dto.MostFoundTypeDto;
import br.com.mercadolivre.statistic.dto.RequestDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;

    @Autowired
    private RequestFunction function;


    public List<RequestMessage> findAll() {
        return this.repository.findAll();
    }


    public RequestDto getStatisticAboutMostAmount() {

        Map<Long, RequestMessage> amountMap = this.function.getAmountMap(
                this.repository.getCountByTargetPathAndOriginIp()
        );

        Set<String> ips = amountMap.values().stream()
                .map(RequestMessage::getOriginIp)
                .collect(Collectors.toSet());

        Set<String> targets = amountMap.values().stream()
                .map(RequestMessage::getTargetPath)
                .collect(Collectors.toSet());


        MostFoundTypeDto mostFoundIpDTO = function.getMostFoundTypeByMap(amountMap, ips);

        MostFoundTypeDto mostFoundTargetDTO = function.getMostFoundTypeByMap(amountMap, targets);

        MostFoundDto mostFoundBothDto = function.getMostFoundByMap(amountMap);

        return RequestDto.builder()
                .both(mostFoundBothDto)
                .originIp(mostFoundIpDTO)
                .targetPath(mostFoundTargetDTO).build();



    }
}
