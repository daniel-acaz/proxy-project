package br.com.mercadolivre.meliproxy.repository;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
}
