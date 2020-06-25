package br.com.mercadolivre.statistic.service;

import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repository;


    public List<RequestMessage> findAll() {
        return this.repository.findAll();
    }
}
