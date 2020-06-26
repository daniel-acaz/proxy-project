package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.repository.ParameterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static br.com.mercadolivre.proxy.constant.ParameterId.MAIN;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParameterServiceTest {

    @InjectMocks
    private ParameterService service;

    @Mock
    private ParameterRepository repository;

    @Test
    public void shouldCreateParameterSuccess() {

        when(repository.findById(MAIN.getId())).thenReturn(Optional.empty());

        when(repository.save(any(RequestParameter.class))).thenAnswer((Answer<RequestParameter>) invocation -> {
            Object[] args = invocation.getArguments();
            return (RequestParameter) args[0];
        });

        assertThat(service.createSingletonRegistryParameter(),
                hasProperty("id", is(MAIN.getId())));

        assertThat(service.createSingletonRegistryParameter(),
                hasProperty("expirationTime", is(30L)));

    }

    @Test
    public void shouldNotCreateParameterSuccess() {

        RequestParameter mockParameter = RequestParameter.builder()
                .id(MAIN.getId())
                .limitBoth(30)
                .limitOrigin(60)
                .limitTarget(60)
                .expirationTime(30L)
                .build();

        when(repository.findById(MAIN.getId())).thenReturn(Optional.of(mockParameter));

        assertNull(service.createSingletonRegistryParameter());

    }

    @Test
    public void shouldUpdateParameter() {

        RequestParameter mockParameter = RequestParameter.builder()
                .limitBoth(30)
                .limitOrigin(60)
                .limitTarget(60)
                .expirationTime(30L)
                .build();

        when(repository.save(mockParameter)).thenReturn(mockParameter);

        assertThat(service.updateParameter(mockParameter),
                hasProperty("id", is(MAIN.getId())));
    }

    @Test
    public void shouldFindParameterSuccess() {

        RequestParameter mockParameter = RequestParameter.builder()
                .id(MAIN.getId())
                .limitBoth(30)
                .limitOrigin(60)
                .limitTarget(60)
                .expirationTime(30L)
                .build();

        when(repository.findById(MAIN.getId())).thenReturn(Optional.of(mockParameter));

        assertThat(service.findParameters(),
                hasProperty("id", is(MAIN.getId())));
    }

    @Test
    public void shouldNotFindParameterSuccess() {

        when(repository.findById(MAIN.getId())).thenReturn(Optional.empty());

        ParameterNotFoundException exception = assertThrows(ParameterNotFoundException.class,
                () -> service.findParameters());

        assertThat(exception, hasProperty("message",
                is("There is no Parameters to Available Requests")));
    }

}
