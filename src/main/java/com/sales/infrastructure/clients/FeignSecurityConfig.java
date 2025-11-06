package com.sales.infrastructure.clients;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignSecurityConfig implements RequestInterceptor {
    @Override
    public void apply(feign.RequestTemplate template) {
        var ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        var auth = ctx.getAuthentication();
        if (auth instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken jwtAuth) {
            var token = ((org.springframework.security.oauth2.jwt.Jwt) jwtAuth.getToken()).getTokenValue();
            template.header("Authorization", "Bearer " + token);
        }
    }
}