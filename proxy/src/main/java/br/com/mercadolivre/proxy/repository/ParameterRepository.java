package br.com.mercadolivre.proxy.repository;

import br.com.mercadolivre.proxy.model.RequestParameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends CrudRepository<RequestParameter, String> {
}
