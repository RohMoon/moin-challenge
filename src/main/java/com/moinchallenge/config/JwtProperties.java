package com.moinchallenge.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@Getter
@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private final String secretKey;
    private final Long expiration;

    public JwtProperties(String secretKey, Long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }
}
