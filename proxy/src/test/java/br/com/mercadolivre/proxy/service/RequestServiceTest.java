package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ExceededRequestException;
import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.repository.ParameterRepository;
import br.com.mercadolivre.proxy.repository.RedisTemplateRepository;
import br.com.mercadolivre.proxy.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static br.com.mercadolivre.proxy.constant.ParameterId.MAIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @InjectMocks
    private RequestService service;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RedisTemplateRepository redisRepository;

    @Mock
    private ParameterRepository parameterRepository;

    @Test
    public void shouldSaveRequest() {

        RequestEntity mockRequest = RequestEntity.builder()
                .expirationTime(10L)
                .originIp("0.0.0.0:00")
                .requestTime(LocalDateTime.now())
                .targetPath("/api/sites/MLB")
                .build();

        mockRequest.setDefaultId();

        when(requestRepository.save(mockRequest)).thenReturn(mockRequest);

        RequestEntity request = service.saveRequest(mockRequest);

        assertThat(request, hasProperty("id", is(mockRequest.getId())));
    }

    @Test
    public void shouldAllowRequest() throws ExceededRequestException {

        HttpServletRequest http = mock(HttpServletRequest.class);
        when(http.getRemoteAddr()).thenReturn("0.0.0.0:00");
        when(http.getServletPath()).thenReturn("/api/sites/MLB");

        RequestParameter parameter = RequestParameter.builder()
                .limitTarget(10)
                .expirationTime(10L)
                .id(MAIN.getId())
                .limitBoth(10)
                .limitOrigin(10).build();

        when(parameterRepository.findById(MAIN.getId())).thenReturn(Optional.of(parameter));

        when(redisRepository.findAllByKeyRegexPattern("0.0.0.0:00")).thenReturn(buildMockRequestEntitySet(2));
        when(redisRepository.findAllByKeyRegexPattern("/api/sites/MLB")).thenReturn(buildMockRequestEntitySet(2));

        assertThat(service.requestIsAllowed(http), is(true));

    }

    @Test
    public void shouldForbidRequest() {

        HttpServletRequest http = mock(HttpServletRequest.class);
        when(http.getRemoteAddr()).thenReturn("0.0.0.0:00");
        when(http.getServletPath()).thenReturn("/api/sites/MLB");

        RequestParameter parameter = RequestParameter.builder()
                .limitTarget(10)
                .expirationTime(10L)
                .id(MAIN.getId())
                .limitBoth(10)
                .limitOrigin(10).build();

        when(parameterRepository.findById(MAIN.getId())).thenReturn(Optional.of(parameter));
        when(redisRepository.findAllByKeyRegexPattern("0.0.0.0:00")).thenReturn(buildMockRequestEntitySet(12));
        when(redisRepository.findAllByKeyRegexPattern("/api/sites/MLB")).thenReturn(buildMockRequestEntitySet(12));

        ExceededRequestException exception = assertThrows(ExceededRequestException.class,
                () -> service.requestIsAllowed(http));

        assertThat(exception, hasProperty("message", is("You have exceeded the requests limit")));

    }

    @Test
    public void shouldNotFoundParameter() {

        HttpServletRequest http = mock(HttpServletRequest.class);

        when(parameterRepository.findById(MAIN.getId())).thenReturn(Optional.empty());

        ParameterNotFoundException exception = assertThrows(ParameterNotFoundException.class,
                () -> service.requestIsAllowed(http));

        assertThat(exception, hasProperty("message", is("There is no Parameters to Available Requests")));

    }

    private Set<RequestEntity> buildMockRequestEntitySet(int size) {
        Set<RequestEntity> requestEntitySet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            RequestEntity mockRequest = RequestEntity.builder()
                    .id(String.valueOf(i))
                    .expirationTime(10L)
                    .originIp("0.0.0.0:0")
                    .requestTime(LocalDateTime.now())
                    .targetPath("/api/sites/MLB")
                    .build();
            requestEntitySet.add(mockRequest);
        }
        return requestEntitySet;
    }


}
