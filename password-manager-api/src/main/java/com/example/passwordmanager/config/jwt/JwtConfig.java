package com.example.passwordmanager.config.jwt;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private long accessTokenExp;
    private long refreshTokenExp;

    public long getAccessTokenExp() {
        return accessTokenExp * 60 * 60 * 1000;
    }

    public long getRefreshTokenExp() {
        return refreshTokenExp * 60 * 60 * 1000;
    }
}
