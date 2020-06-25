package br.com.mercadolivre.meliproxy.repository;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<RequestEntity, String> {
}
