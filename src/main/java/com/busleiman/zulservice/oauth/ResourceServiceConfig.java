package com.busleiman.zulservice.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
       resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/security/oauth/token").permitAll()
        .antMatchers(HttpMethod.GET, "api/products/products", "api/items/items", "/api/users/users").permitAll()
        .antMatchers(HttpMethod.GET, "/api/products/products/{id}", "/api/items/items/{id}/quantity/{quantity}}", "/api/users/users/{id}").hasAnyRole("ADMIN")
        .antMatchers(HttpMethod.POST, "api/products/products/", "api/items/items/", "/api/users/users/").hasAnyRole("ADMIN")
        .anyRequest().authenticated();
    }


    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter =  new JwtAccessTokenConverter();

        jwtAccessTokenConverter.setSigningKey("codigo_secreto");

        return jwtAccessTokenConverter;
    }
}
