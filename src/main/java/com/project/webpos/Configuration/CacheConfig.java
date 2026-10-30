package com.project.webpos.Configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration 
@ConfigurationProperties(prefix = "app.cache")
public class CacheConfig {
  private Duration defaultTtl = Duration.ofMinutes(10);
}
