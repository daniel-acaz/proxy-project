package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.model.RequestEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class ConverterService {

    public RequestEntity buildEntityByRequest(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String remotePath = request.getServletPath();

        return RequestEntity.builder()
                .originIp(remoteAddr)
                .targetPath(remotePath)
                .requestTime(LocalDateTime.now())
                .expirationTime(30L)
                .build();
    }
}
