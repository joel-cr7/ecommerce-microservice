package com.ecommerce.api_gateway.filters;

import com.ecommerce.api_gateway.service.JWTService;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JWTService jwtService;

    public AuthenticationGatewayFilterFactory(JWTService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if(!config.enabled) return chain.filter(exchange);

            // get the token from request header
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authorizationHeader==null){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();    // return the response from here itself
            }
            String token = authorizationHeader.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);

            // mutate (modify certain parts of request)
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId.toString())
                    .build();

            // pass the mutated request to further filters
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    @Data
    public static class Config{
        private boolean enabled;
    }
}
