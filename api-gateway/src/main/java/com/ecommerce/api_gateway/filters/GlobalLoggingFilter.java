package com.ecommerce.api_gateway.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger log = LogManager.getLogger(GlobalLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // applying pre-filter
        log.info("Logging from Global pre-filter: {}", exchange.getRequest().getURI());

        // call next filter in chain, and in then() mention post-filter
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Logging from Global post-filter {}", exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        return 5;
    }
}
