package com.liro.zuulservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException {

        /*
          This is a pojo you can create to hold the repsonse code, error, and description.
          You can create a POJO to hold whatever information you want to send back.
        */
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, "Log in error", "Authentication error");

        System.out.println("paso por acaaaaaaaaaaaaa");
        /*
          Here we're going to creat a json strong from the CustomError object we just created.
          We set the media type, encoding, and then get the write from the response object and write
      our json string to the response.
        */

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json =objectMapper.writeValueAsString(error);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().write(json);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}