package com.lightlyshiny.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.frontend")
@Getter
@Setter
public class FrontEndProperties {
    private String url;
}