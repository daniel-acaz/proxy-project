package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static br.com.mercadolivre.proxy.constant.ParameterId.MAIN;

@Service
public class ConverterService {

    @Autowired
    private ParameterRepository repository;

    public RequestEntity buildEntityByRequest(HttpServletRequest request) {
        RequestParameter parameter = repository.findById(MAIN.getId())
                .orElseThrow(ParameterNotFoundException::new);

        String remoteAddr = request.getRemoteAddr();
        String remotePath = request.getServletPath();

        RequestEntity requestEntity = RequestEntity.builder()
                .originIp(remoteAddr)
                .targetPath(remotePath)
                .requestTime(LocalDateTime.now())
                .expirationTime(parameter.getExpirationTime())
                .build();

        requestEntity.setDefaultId();

        return requestEntity;
    }
}
