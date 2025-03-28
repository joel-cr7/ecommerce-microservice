package com.ecommerce.api_gateway.config;


import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Capability capability(MeterRegistry meterRegistry){
        return new MicrometerCapability(meterRegistry);
    }
}
