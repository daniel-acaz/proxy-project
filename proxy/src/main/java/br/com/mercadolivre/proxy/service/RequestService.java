package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ExceededRequestException;
import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.repository.ParameterRepository;
import br.com.mercadolivre.proxy.repository.RedisTemplateRepository;
import br.com.mercadolivre.proxy.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.mercadolivre.proxy.constant.ParameterId.MAIN;

@Service
@Slf4j
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RedisTemplateRepository redisRepository;

    @Autowired
    private ParameterRepository parameterRepository;

    public RequestEntity saveRequest(RequestEntity requestEntity) {

        return requestRepository.save(requestEntity);

    }

    public boolean requestIsAllowed(HttpServletRequest request) throws ExceededRequestException {

        String originIp = request.getRemoteAddr();
        String targetPath = request.getServletPath();

        RequestParameter parameter = parameterRepository.findById(MAIN.getId())
                .orElseThrow(ParameterNotFoundException::new);

        Set<RequestEntity> originIps = redisRepository.findAllByKeyPart(originIp);

        Set<RequestEntity> targetPaths = redisRepository.findAllByKeyPart(targetPath);

        Set<RequestEntity> both = originIps.stream()
                .filter(requestEntity -> requestEntity.getTargetPath().equals(targetPath))
                .collect(Collectors.toSet());

        if( originIps.size() >= parameter.getLimitOrigin()
                || targetPaths.size() >= parameter.getLimitTarget()
                || both.size() >= parameter.getLimitBoth()) {

            log.error("isn't allowed");
            throw new ExceededRequestException();
        }

        return true;
    }
}
