package com.moinchallenge.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@Getter
@AllArgsConstructor
@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {
    private final String secretKey;
}
