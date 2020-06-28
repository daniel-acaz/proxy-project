package br.com.mercadolivre.proxy.controller;

import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestParameter;
import br.com.mercadolivre.proxy.service.ParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("parameters")
@Api(tags = "Parameter", description = "Endpoint to Update and Get parameters")
public class ParameterController {

    @Autowired
    private ParameterService service;

    @CrossOrigin(origins = "*")
    @PutMapping(produces = APPLICATION_JSON_VALUE)
    @ApiResponses(
        @ApiResponse(code = 200, message = "Success", response = RequestParameter.class)
    )
    public ResponseEntity<?> updateParameters(@RequestBody RequestParameter parameter) {
        return ResponseEntity.ok(service.updateParameter(parameter));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiResponses(
            {
            @ApiResponse(code = 200, message = "Success", response = RequestParameter.class),
            @ApiResponse(code = 404, message = "There is no Parameters to Available Requests")
            }
    )
    public ResponseEntity<?> getParameters() {
        try {
            return ResponseEntity.ok(service.findParameters());
        } catch (ParameterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
