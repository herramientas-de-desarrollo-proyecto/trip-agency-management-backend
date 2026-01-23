package com.tripagencymanagement.template.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

    private static final Logger log = LoggerFactory.getLogger(PageableConfig.class);

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableResolverCustomizer() {
        log.info("Configuring pagination with 1-based indexing");
        return pageableResolver -> pageableResolver.setOneIndexedParameters(true);
    }
}
