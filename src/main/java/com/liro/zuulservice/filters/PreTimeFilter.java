package com.liro.zuulservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        String username;

        RequestContext rx = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = rx.getRequest();

        Long currentTime = System.currentTimeMillis();
        httpServletRequest.setAttribute("currentTime", currentTime);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        logger.info("USERNAME: " + username);

        return null;
    }
}
