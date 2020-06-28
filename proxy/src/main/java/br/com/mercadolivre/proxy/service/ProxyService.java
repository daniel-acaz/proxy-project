package br.com.mercadolivre.proxy.service;

import br.com.mercadolivre.proxy.error.ExceededRequestException;
import br.com.mercadolivre.proxy.error.ParameterNotFoundException;
import br.com.mercadolivre.proxy.model.RequestEntity;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@Slf4j
public class ProxyService extends ZuulFilter {

    @Autowired
    private RequestService requestService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConverterService converterService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();

        RequestEntity entity = this.converterService.buildEntityByRequest(request);

        boolean allowed = false;

        try {

            allowed = requestService.requestIsAllowed(request);
            requestService.saveRequest(entity);

        } catch (ExceededRequestException e) {

            throw new ZuulException(e, FORBIDDEN.value(), e.getMessage());

        } catch (ParameterNotFoundException e) {

            throw new ZuulException(e, INTERNAL_SERVER_ERROR.value(), e.getMessage());

        } finally {

            messageService.sendRequest(entity, allowed);

        }

        return null;
    }
}
