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
                .antMatchers( "/api/pets/h2-console/**", "/api/users/h2-console/**",  "/api/pets/actuator/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/users").permitAll()
                .antMatchers(HttpMethod.GET, "api/products/products", "api/items/items", "/api/users/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/products/{id}", "/api/items/items/{id}/quantity/{quantity}}", "/api/users/users/{id}").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "api/products/products/", "api/items/items/").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
    }


    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        jwtAccessTokenConverter.setSigningKey("codigo_secreto");

        return jwtAccessTokenConverter;
    }
}
