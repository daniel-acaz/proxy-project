package br.com.mercadolivre.statistic.service;

import br.com.mercadolivre.statistic.dto.MostFoundDto;
import br.com.mercadolivre.statistic.dto.MostFoundTypeDto;
import br.com.mercadolivre.statistic.dto.RequestDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @InjectMocks
    private RequestService service;

    @Mock
    private RequestRepository repository;

    @Mock
    private RequestFunction function;

    @Test
    public void shouldGetStatisticAboutAmount() {

        String target1 = "TARGET 1";
        String ip1 = "IP 1";
        String target2 = "TARGET 2";
        String ip2 = "IP 2";
        String target3 = "TARGET 3";
        String ip3 = "IP 3";

        List<Object[]> resultSet = Arrays.asList(
                new Object[] {300L, target1, ip1},
                new Object[] {2000L, target2, ip2},
                new Object[] {100L, target1, ip2},
                new Object[] {20L, target2, ip1},
                new Object[] {30L, target3, ip3}
        );

        Map<Long, RequestMessage> resultQuery = new HashMap();

        resultQuery.put(300L, RequestMessage.builder().originIp(ip1).targetPath(target1).build());
        resultQuery.put(2000L, RequestMessage.builder().originIp(ip2).targetPath(target2).build());
        resultQuery.put(100L, RequestMessage.builder().originIp(ip2).targetPath(target1).build());
        resultQuery.put(20L, RequestMessage.builder().originIp(ip1).targetPath(target2).build());
        resultQuery.put(30L, RequestMessage.builder().originIp(ip3).targetPath(target3).build());

        Set<String> ips = new HashSet<>(Arrays.asList(ip1, ip2, ip3));

        Set<String> targets = new HashSet<>(Arrays.asList(target1, target2, target3));

        MostFoundTypeDto mostFoundIpDto = MostFoundTypeDto.builder().amount(2100L).type(ip2).build();
        MostFoundTypeDto mostFoundTargetDto = MostFoundTypeDto.builder().amount(2020L).type(target2).build();
        MostFoundDto mostFoundBothDto = MostFoundDto.builder().originIp(ip2).targetPah(target2).build();

        when(repository.getCountByTargetPathAndOriginIp()).thenReturn(resultSet);

        when(function.getAmountMap(resultSet)).thenReturn(resultQuery);

        doReturn(mostFoundIpDto).when(function).getMostFoundTypeByMap(resultQuery, ips);
        doReturn(mostFoundTargetDto).when(function).getMostFoundTypeByMap(resultQuery, targets);
        when(function.getMostFoundByMap(resultQuery)).thenReturn(mostFoundBothDto);

         RequestDto statisticAboutMostAmount = service.getStatisticAboutMostAmount();

        assertThat(statisticAboutMostAmount.getBoth(), is(mostFoundBothDto));
        assertThat(statisticAboutMostAmount.getOriginIp(), is(mostFoundIpDto));
        assertThat(statisticAboutMostAmount.getTargetPath(), is(mostFoundTargetDto));


    }

}
