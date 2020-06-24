package br.com.mercadolivre.meliproxy.filter;

import br.com.mercadolivre.meliproxy.model.RequestEntity;
import br.com.mercadolivre.meliproxy.repository.RequestRepository;
import br.com.mercadolivre.meliproxy.service.RequestService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
@Slf4j
public class XForwardedForFilter extends ZuulFilter {

    @Autowired
    private RequestService service;

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

        service.saveRequest(request);

        return null;
    }
}
