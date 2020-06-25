package br.com.mercadolivre.proxy.controller;

import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("parameters")
public class ParameterController {

    @Autowired
    private ParameterService service;

    @PutMapping
    public ResponseEntity<?> updateParameters(@RequestBody RequestParameter parameter) {
        return ResponseEntity.ok(service.updateParameter(parameter));
    }

    @GetMapping
    public ResponseEntity<?> getParameters() {
        return ResponseEntity.ok(service.findParameters());
    }


}
