package br.com.mercadolivre.meliproxy.repository;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    int countByOriginIpAndRequestTimeAfter(String originIp, LocalDateTime requestTime);

    int countByTargetPathAndRequestTimeAfter(String targetPath, LocalDateTime requestTime);

    int countByOriginIpAndTargetPathAndRequestTimeAfter(String originIp, String targetPath, LocalDateTime requestTime);

}
