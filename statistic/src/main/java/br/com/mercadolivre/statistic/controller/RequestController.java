package br.com.mercadolivre.statistic.controller;

import br.com.mercadolivre.statistic.dto.RequestDto;
import br.com.mercadolivre.statistic.model.RequestMessage;
import br.com.mercadolivre.statistic.service.RequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("requests")
@Api(tags = "Requests", description = "Endpoint to take Analysis and Statistics of requests")
public class RequestController {

    @Autowired
    private RequestService service;

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Success", response = RequestMessage.class, responseContainer = "List")
    )
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/most", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Success", response = RequestDto.class, responseContainer = "List")
    )
    public ResponseEntity<?> getMostAmountFoundStatistic() {
        return ResponseEntity.ok(service.getStatisticAboutMostAmount());
    }


}
