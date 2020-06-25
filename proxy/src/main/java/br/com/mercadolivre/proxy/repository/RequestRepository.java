package br.com.mercadolivre.proxy.repository;

import br.com.mercadolivre.proxy.model.RequestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<RequestEntity, String> {
}
