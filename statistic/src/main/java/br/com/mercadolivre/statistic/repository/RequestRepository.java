package br.com.mercadolivre.statistic.repository;

import br.com.mercadolivre.statistic.model.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RequestRepository extends JpaRepository<RequestMessage, Long> {

    @Query(value = "select count(request), request.targetPath, request.originIp " +
            "from RequestMessage request " +
            "group by request.targetPath, request.originIp " +
            "order by count(request) desc")
    List<Object[]> getCountByTargetPathAndOriginIp();

}
