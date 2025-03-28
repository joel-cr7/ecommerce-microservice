package com.ecommerce.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class OrdersLoggingFilter extends AbstractGatewayFilterFactory<OrdersLoggingFilter.Config> {

    private static final Logger log = LogManager.getLogger(OrdersLoggingFilter.class);

    // implement the constructor
    public OrdersLoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // As GatewayFilter is interface with 1 method we can use lambda
        return (exchange, chain) -> {

            // apply the pre-filter operations
            log.info("Order filter Pre: {}", exchange.getRequest().getURI());

            return chain.filter(exchange);      // call next filter in the chain
        };
    }

    public static class Config{

    }
}
