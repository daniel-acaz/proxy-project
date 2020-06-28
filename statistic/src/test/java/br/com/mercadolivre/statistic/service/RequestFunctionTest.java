package br.com.mercadolivre.statistic.service;

import br.com.mercadolivre.statistic.dto.MostFoundDto;
import br.com.mercadolivre.statistic.dto.MostFoundTypeDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestFunctionTest {

    private RequestFunction service;

    @BeforeEach
    public void init() {
        service = new RequestFunction();
    }

    @Test
    void shouldConverterResultSetToMap() {

        List<Object[]> resultSet = Arrays.asList(
                new Object[] {300L, "TARGET 1","IP 1"},
                new Object[] {2000L, "TARGET 2", "IP 2"},
                new Object[] {100L, "TARGET 1","IP 2"},
                new Object[] {20L, "TARGET 2", "IP 1"}
        );

        Map<Long, RequestMessage> map = service.getAmountMap(resultSet);

        assertThat(map, hasKey(300L));
        assertThat(map.get(300L), hasProperty(
           "targetPath", is("TARGET 1")
        ));
    }

    @Test
    public void shouldReturnTheMostTypeFound() {

        Map<Long, RequestMessage> resultQuery = new HashMap();

        resultQuery.put(1000L, RequestMessage.builder().originIp("IP 1").build());
        resultQuery.put(20L, RequestMessage.builder().originIp("IP 2").build());
        resultQuery.put(10L, RequestMessage.builder().originIp("IP 1").build());
        resultQuery.put(10000L, RequestMessage.builder().originIp("IP 2").build());
        resultQuery.put(1000L, RequestMessage.builder().originIp("IP 3").build());

        Set<String> ips = new HashSet<>();
        ips.add("IP 1");
        ips.add("IP 2");
        ips.add("IP 3");

        MostFoundTypeDto dto = service.getMostFoundTypeByMap(resultQuery, ips);

        assertThat(dto, hasProperty("type", is("IP 2")));
        assertThat(dto, hasProperty("amount", is(10020L)));
    }

    @Test
    public void shouldNotFoundSomeAmount() {

        Map<Long, RequestMessage> resultQuery = new HashMap();
        Set<String> ips = new HashSet<>();

        MostFoundTypeDto mostFoundByMap = service.getMostFoundTypeByMap(resultQuery, ips);

        assertNull(mostFoundByMap.getType());
    }

    @Test
    public void shouldReturnTheMostFound() {

        Map<Long, RequestMessage> resultQuery = new HashMap();

        resultQuery.put(1000L, RequestMessage.builder().originIp("IP 1").targetPath("TARGET 1").build());
        resultQuery.put(20L, RequestMessage.builder().originIp("IP 2").targetPath("TARGET 1").build());
        resultQuery.put(10L, RequestMessage.builder().originIp("IP 1").targetPath("TARGET 2").build());
        resultQuery.put(10000L, RequestMessage.builder().originIp("IP 2").targetPath("TARGET 2").build());
        resultQuery.put(1000L, RequestMessage.builder().originIp("IP 3").targetPath("TARGET 3").build());

        MostFoundDto dto = service.getMostFoundByMap(resultQuery);

        assertThat(dto, hasProperty("targetPah", is("TARGET 2")));
        assertThat(dto, hasProperty("originIp", is("IP 2")));
        assertThat(dto, hasProperty("amount", is(10000L)));
    }

    @Test
    public void shouldNotFoundTheMost() {

        Map<Long, RequestMessage> resultQuery = new HashMap();

        MostFoundDto dto = service.getMostFoundByMap(resultQuery);

        assertNull(dto.getOriginIp());
        assertNull(dto.getTargetPah());
        assertNull(dto.getAmount());
    }




}
