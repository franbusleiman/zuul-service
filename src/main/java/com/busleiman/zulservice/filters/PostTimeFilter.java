package com.busleiman.zulservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;


@Component
public class PostTimeFilter extends ZuulFilter {

   private static Logger log =  LoggerFactory.getLogger(PostTimeFilter.class);

    @Override
    public String filterType() {
        return "post";
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

        RequestContext rx = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = rx.getRequest();


       Long initialTime =  (Long)  httpServletRequest.getAttribute("currentTime");
        Long postTime = System.currentTimeMillis();
        Long  time = postTime -  initialTime;


        log.info(String.valueOf(time.doubleValue()));


        return null;
    }
}
