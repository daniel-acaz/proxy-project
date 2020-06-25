package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository repository;

    @InjectMocks
    private RequestService service;


    @Test
    public void shouldConverterRequestApiToEntity() {

        String targetPathMock = "/api/sites/MLB/categories";
        String originIpMock = "0.0.0.0:0000";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn(originIpMock);
        when(request.getServletPath()).thenReturn(targetPathMock);

        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        RequestEntity requestEntity = service.saveRequest(request);


        assertThat(requestEntity, hasProperty("originIp", is(originIpMock)));
        assertThat(requestEntity, hasProperty("targetPath", is(targetPathMock)));
        assertThat(requestEntity.getRequestTime().toLocalDate(), is(LocalDate.now()));

    }
}
