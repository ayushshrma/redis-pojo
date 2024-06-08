package com.demo;

import com.demo.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
@RequiredArgsConstructor
public class Application {

    private final AppConfig appConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer(Environment env) {
        return (resolver) -> {
            resolver.setFallbackPageable(PageRequest.of(0, appConfig.getPaging().getDefaultPageSize()));
            resolver.setMaxPageSize(appConfig.getPaging().getMaxPageSize());
            resolver.setPageParameterName(appConfig.getPaging().getPageParameter());
            resolver.setSizeParameterName(appConfig.getPaging().getSizeParameter());
        };
    }
}
