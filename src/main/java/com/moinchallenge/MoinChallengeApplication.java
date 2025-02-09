package com.moinchallenge;

import com.moinchallenge.config.EncryptionProperties;
import com.moinchallenge.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, EncryptionProperties.class})
public class MoinChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoinChallengeApplication.class, args);
    }
}
