package com.busleiman.zulservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreTimeFilter extends ZuulFilter {

    Logger logger = LoggerFactory.getLogger(PreTimeFilter.class);


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        logger.info("Crossing the edge");

        RequestContext rx = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = rx.getRequest();

        Long currentTime = System.currentTimeMillis();
        httpServletRequest.setAttribute("currentTime", currentTime);

        return null;
    }
}
