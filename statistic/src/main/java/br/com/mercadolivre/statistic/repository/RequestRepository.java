package br.com.mercadolivre.statistic.repository;

import br.com.mercadolivre.statistic.model.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<RequestMessage, Long> {
}
