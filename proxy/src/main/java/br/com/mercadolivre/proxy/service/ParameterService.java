package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.repository.ParameterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.mercadolivre.proxy.constant.ParameterId.MAIN;

@Service
@Slf4j
public class ParameterService {

    @Autowired
    private ParameterRepository repository;

    public RequestParameter createSingletonRegistryParameter() {

        log.info("[STARTUP] parameters will be created");

        if(repository.findById(MAIN.getId()).isPresent())
            return null;

        RequestParameter parameter = RequestParameter.builder()
                .id(MAIN.getId())
                .limitBoth(30)
                .limitOrigin(60)
                .limitTarget(60)
                .expirationTime(30L)
                .build();

        log.info("[STARTUP] parameters created");
        return repository.save(parameter);

    }

    public RequestParameter updateParameter(RequestParameter parameter) {
        parameter.setId(MAIN.getId());
        repository.save(parameter);
        return parameter;
    }

    public RequestParameter findParameters() {
        return repository.findById(MAIN.getId())
                .orElseThrow(ParameterNotFoundException::new);
    }
}
