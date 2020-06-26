package br.com.mercadolivre.proxy.controller;

import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.service.ParameterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParameterControllerTest {

    @InjectMocks
    private ParameterController controller;

    @Mock
    private ParameterService service;

    @Test
    public void shouldUpdateParametersSuccess() {

        RequestParameter mockParameter = RequestParameter.builder()
                .limitBoth(10)
                .limitOrigin(10)
                .limitTarget(10)
                .expirationTime(10L)
                .build();

        when(service.updateParameter(mockParameter)).thenReturn(mockParameter);

        assertThat(controller.updateParameters(mockParameter),
                hasProperty("statusCode", is(HttpStatus.OK)));
    }

    @Test
    public void shouldUFindParametersSuccess() {

        RequestParameter mockParameter = RequestParameter.builder()
                .limitBoth(10)
                .limitOrigin(10)
                .limitTarget(10)
                .expirationTime(10L)
                .build();

        when(service.findParameters()).thenReturn(mockParameter);

        assertThat(controller.getParameters(),
                hasProperty("statusCode", is(HttpStatus.OK)));

        assertThat(controller.getParameters(),
                hasProperty("body", is(mockParameter)));
    }


}
