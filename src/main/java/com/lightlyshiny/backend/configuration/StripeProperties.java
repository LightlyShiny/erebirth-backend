package com.lightlyshiny.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.stripe")
@Getter
@Setter
public class StripeProperties {
    private String apiSecret;
    private String cancelUrl;
    private String subscriptionCurrency;
    private Long subscriptionPrice;
    private String successUrl;
    private String webhookSecret;
}