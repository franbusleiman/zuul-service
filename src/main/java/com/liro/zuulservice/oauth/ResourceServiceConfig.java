package com.liro.zuulservice.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(ResourceServiceConfig.class);

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/api/security/oauth/token").permitAll()
                .antMatchers("/api/users/h2-console/**").permitAll()
                .antMatchers("/api/users/users/setAccount").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/users/users/existsByEmail/{email}").permitAll()
                .antMatchers("/api/users/users/existsByIdentificationNr/{identificationNr}").permitAll()
                .antMatchers("/api/users/h2-console/**").permitAll()
                .antMatchers("/api/animals/h2-console/**").permitAll()
                .antMatchers("/api/medicines/h2-console/**").permitAll()
                .antMatchers("/api/applications/h2-console/**").permitAll()
                .antMatchers("/api/consultations/h2-console/**").permitAll()
                .antMatchers("**/swagger-resources/**").permitAll()
                .antMatchers("**/v2/api-docs").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api/users/v2/api-docs").permitAll()
                .antMatchers("/api/animals/v2/api-docs").permitAll()
                .antMatchers("/api/migrator/v2/api-docs").permitAll()
                .antMatchers("/api/animals/swagger-ui.html").permitAll()
                .antMatchers("/api/medicines/v2/api-docs").permitAll()
                .antMatchers("/api/medicines/swagger-ui.html").permitAll()
                .antMatchers("/api/consultations/v2/api-docs").permitAll()
                .antMatchers("/api/consultations/swagger-ui.html").permitAll()
                .antMatchers("/api/applications/v2/api-docs").permitAll()
                .antMatchers("/api/applications/swagger-ui.html").permitAll()
                .antMatchers("/api/users/swagger-ui.html").permitAll()
                .antMatchers("/api/animals/swagger-ui/index.html").permitAll()
                .antMatchers("/api/users/swagger-ui/index.html").permitAll()
                .antMatchers("/swagger-ui/index.html").permitAll()
                .antMatchers("/api/clinics/swagger-ui.html").permitAll()
                .antMatchers("/api/clinics/swagger-ui/index.html").permitAll()
                .antMatchers("/api/clinics/v2/api-docs").permitAll()
                .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/users").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/prelunch").permitAll()
                .antMatchers(HttpMethod.POST, "/api/animals/recordTypes").hasAnyRole("VET","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/animals/types").hasAnyRole("VET","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/animals/animalColors").hasAnyRole("VET","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/animals/breeds").hasAnyRole("VET","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/consultations/consultations").hasAnyRole("VET","ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/consultations/consultations").hasAnyRole("VET","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clinics/clinics").hasAnyRole("VET", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfigurationSource source = new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest httpServletRequest) {

                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                config.addAllowedOrigin("https://app.liro.pet");
                config.addAllowedOrigin("https://liro.pet");
                config.addAllowedOrigin("https://api.liro.pet");
                config.addAllowedOrigin("https://www.liro.pet");// Cambia por tu dominio
                config.addAllowedHeader("Authorization");
                config.addAllowedMethod(HttpMethod.OPTIONS.name());
                config.addAllowedMethod("POST");
                config.addAllowedMethod("PUT");
                config.addAllowedMethod("DELETE");
                config.addAllowedMethod("GET");
                config.addAllowedHeader("Content-Type");
                config.addAllowedHeader("*");
                return config;
            }
        };

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) throws InvalidTokenException {
                try {
                    return super.extractAccessToken(value, map);
                } catch (Exception e) {
                    // Añadir log en caso de excepción
                    Logger logger = LoggerFactory.getLogger(JwtAccessTokenConverter.class);
                    logger.error("Error al extraer el token de acceso", e);
                    throw e;
                }
            }

            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {

                return super.extractAuthentication(map);
            }
        };
        jwtAccessTokenConverter.setSigningKey("asdfAEGVDSAkdnASBOIAW912927171Q23Q");
        return jwtAccessTokenConverter;
    }


    @Component
    @Primary
    @EnableAutoConfiguration
    public static class DocumentationController implements SwaggerResourcesProvider {

        @Override
        public List get() {
            List resources = new ArrayList<>();
            resources.add(swaggerResource("users-service", "/api/users/v2/api-docs", "2.0"));
            resources.add(swaggerResource("animals-service", "/api/animals/v2/api-docs", "2.0"));
            resources.add(swaggerResource("medicines-service", "/api/medicines/v2/api-docs", "2.0"));
            resources.add(swaggerResource("consultations-service", "/api/consultations/v2/api-docs", "2.0"));
            resources.add(swaggerResource("migrator-service", "/api/migrator/v2/api-docs", "2.0"));
            resources.add(swaggerResource("clinics-service", "/api/clinics/v2/api-docs", "2.0"));



            return resources;
        }

        private SwaggerResource swaggerResource(String name, String location, String version) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setLocation(location);
            swaggerResource.setSwaggerVersion(version);
            return swaggerResource;
        }

    }
}
