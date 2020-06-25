package br.com.mercadolivre.proxy.filter;

import br.com.mercadolivre.proxy.model.RequestEntity;
import br.com.mercadolivre.proxy.service.ConverterService;
import br.com.mercadolivre.proxy.service.MessageService;
import br.com.mercadolivre.proxy.service.RequestService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class XForwardedForFilter extends ZuulFilter {

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
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();

        RequestEntity entity = this.converterService.buildEntityByRequest(request);


        boolean allowed = requestService.isAllowed(request);

        if(allowed) {
            requestService.saveRequest(entity);
        }

        messageService.sendRequest(entity, allowed);

        return null;
    }
}
