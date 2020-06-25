package br.com.mercadolivre.statistic.controller;

import br.com.mercadolivre.statistic.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("requests")
public class RequestController {

    @Autowired
    private RequestService service;

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(service.findAll());
    }

}
