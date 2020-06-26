package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.repository.ParameterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static br.com.mercadolivre.proxy.constant.ParameterId.MAIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConverterServiceTest {

    @InjectMocks
    private ConverterService service;

    @Mock
    private ParameterRepository repository;

    @Test
    public void shouldBuildEntityByRequest() {

        HttpServletRequest http = mock(HttpServletRequest.class);

        RequestParameter parameter = RequestParameter.builder()
                .id(MAIN.getId())
                .expirationTime(10L)
                .limitTarget(10)
                .limitOrigin(10)
                .limitBoth(10)
                .build();

        when(repository.findById(MAIN.getId())).thenReturn(Optional.of(parameter));

        when(http.getRemoteAddr()).thenReturn("0.0.0.0");
        when(http.getServletPath()).thenReturn("/api/sites/MLB");

        RequestEntity requestEntity = service.buildEntityByRequest(http);

        assertThat(requestEntity, hasProperty("expirationTime", is(parameter.getExpirationTime())));
        assertThat(requestEntity, hasProperty("originIp", is("0.0.0.0")));

    }

    @Test
    public void shouldNotFoundParameter() {

        HttpServletRequest http = mock(HttpServletRequest.class);

        when(repository.findById(MAIN.getId())).thenReturn(Optional.empty());

        ParameterNotFoundException exception = assertThrows(ParameterNotFoundException.class,
                () -> service.buildEntityByRequest(http));

        assertThat(exception, hasProperty("message",
                is("There is no Parameters to Available Requests")));

    }

}
