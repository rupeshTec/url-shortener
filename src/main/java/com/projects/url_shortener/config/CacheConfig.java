package com.projects.url_shortener.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public com.github.benmanes.caffeine.cache.Cache<String, String> caffeineCache() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)  // Max 10,000 entries
                .expireAfterWrite(24, TimeUnit.HOURS)  // Expire after 24 hours
                .build();
    }
}
