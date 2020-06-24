package br.com.mercadolivre.meliproxy.service;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import br.com.mercadolivre.meliproxy.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;

    public RequestEntity saveRequest(HttpServletRequest request) {

        String remoteAddr = request.getRemoteAddr();
        String remotePath = request.getServletPath();

        RequestEntity requestEntity = RequestEntity.builder()
                .originIp(remoteAddr)
                .targetPath(remotePath)
                .requestTime(LocalDateTime.now())
                .build();

        return repository.save(requestEntity);

    }
}
