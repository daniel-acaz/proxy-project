package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.repository.RedisTemplateRepository;
import br.com.mercadolivre.proxy.repository.RequestRepository;
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

    public RequestEntity saveRequest(RequestEntity requestEntity) {

        requestEntity.setDefaultId();

        return requestRepository.save(requestEntity);

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
