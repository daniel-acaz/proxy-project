package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ExceededRequestException;
import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestEntity;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.monitoring.CounterFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.netflix.zuul.metrics.EmptyCounterFactory;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
public class ProxyServiceTest {

    @InjectMocks
    private ProxyService service;

    @Mock
    private ConverterService converterService;

    @Mock
    private RequestService requestService;

    @BeforeAll
    public static void setUp() {
        CounterFactory.initialize(new EmptyCounterFactory());
    }

    @Test
    public void shouldRunSuccessProxy() throws ExceededRequestException, ZuulException {

        RequestEntity mockRequest = RequestEntity.builder()
                .expirationTime(10L)
                .originIp("0.0.0.0:00")
                .requestTime(LocalDateTime.now())
                .targetPath("/api/sites/MLB")
                .build();

        when(converterService.buildEntityByRequest(any())).thenReturn(mockRequest);

        when(requestService.requestIsAllowed(any())).thenReturn(true);

        when(requestService.saveRequest(mockRequest)).thenReturn(mockRequest);

        assertNull(service.run());
    }

    @Test
    public void shouldThrowExceptionToExceededRequest() throws ExceededRequestException {

        RequestEntity mockRequest = RequestEntity.builder()
                .expirationTime(10L)
                .originIp("0.0.0.0:00")
                .requestTime(LocalDateTime.now())
                .targetPath("/api/sites/MLB")
                .build();

        when(converterService.buildEntityByRequest(any())).thenReturn(mockRequest);

        when(requestService.requestIsAllowed(any())).thenThrow(ExceededRequestException.class);

        ZuulException exception = assertThrows(ZuulException.class,
                () -> service.run());

        assertThat(exception.nStatusCode, is(FORBIDDEN.value()));
    }

    @Test
    public void shouldThrowExceptionToParameterNotFound() throws ExceededRequestException {

        RequestEntity mockRequest = RequestEntity.builder()
                .expirationTime(10L)
                .originIp("0.0.0.0:00")
                .requestTime(LocalDateTime.now())
                .targetPath("/api/sites/MLB")
                .build();

        when(converterService.buildEntityByRequest(any())).thenReturn(mockRequest);

        when(requestService.requestIsAllowed(any())).thenThrow(ParameterNotFoundException.class);

        ZuulException exception = assertThrows(ZuulException.class,
                () -> service.run());

        assertThat(exception.nStatusCode, is(INTERNAL_SERVER_ERROR.value()));
    }

}
