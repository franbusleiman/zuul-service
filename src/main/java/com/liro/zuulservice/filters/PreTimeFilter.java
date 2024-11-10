package com.liro.zuulservice.filters;

import com.liro.zuulservice.clients.ClinicsService;
import com.liro.zuulservice.clients.FeignClinicClient;
import com.liro.zuulservice.dtos.ClinicaResponse;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PreTimeFilter extends ZuulFilter {

    Logger logger = LoggerFactory.getLogger(PreTimeFilter.class);

    @Autowired
    ClinicsService clinicsService;


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


        RequestContext rx = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = rx.getRequest();

        Long currentTime = System.currentTimeMillis();
        httpServletRequest.setAttribute("currentTime", currentTime);

        String clinicId = httpServletRequest.getHeader("clinicId");

        if(clinicId!=null){
            String token = httpServletRequest.getHeader("Authorization");

            List<ClinicaResponse> clinicaResponseList = clinicsService.getUsersByClinicId(token);


            List<Long> clinicIds = clinicaResponseList.stream().map(ClinicaResponse::getId)
                    .collect(Collectors.toList());

            if(!clinicIds.contains(Long.valueOf(clinicId))){
                rx.unset();
                rx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }

        return null;
    }
}
