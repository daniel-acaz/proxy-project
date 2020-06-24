package br.com.mercadolivre.meliproxy.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class XForwardedForFilter extends ZuulFilter {

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
        String remoteAddr = request.getRemoteAddr();
        String remotePath = request.getServletPath();

        log.info("IP Address: {}", remoteAddr);
        log.info("Destiny Path: {}", remotePath);


        return null;
    }
}
