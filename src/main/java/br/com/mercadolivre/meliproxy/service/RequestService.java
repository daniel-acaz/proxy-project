package br.com.mercadolivre.meliproxy.service;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import br.com.mercadolivre.meliproxy.repository.RedisTemplateRepository;
import br.com.mercadolivre.meliproxy.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RedisTemplateRepository redisRepository;

    public RequestEntity saveRequest(HttpServletRequest request) {

        RequestEntity requestEntity = buildEntityByRequest(request);

        requestEntity.setDefaultId();

        return requestRepository.save(requestEntity);

    }

    private RequestEntity buildEntityByRequest(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String remotePath = request.getServletPath();

        return RequestEntity.builder()
                .originIp(remoteAddr)
                .targetPath(remotePath)
                .requestTime(LocalDateTime.now())
                .expirationTime(30L)
                .build();
    }

    public boolean isAllowed(HttpServletRequest request) {

        String originIp = request.getRemoteAddr();

        Set<RequestEntity> requests = redisRepository.findAllByOriginIp(originIp);

        if(requests.size() == 5) {
            log.error("isn't allowed");
            return false;
        }

        return true;
    }
}
