package com.gateway.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@NoArgsConstructor
public class StaticProperties {
    public static String AUTH_SERVER_URL  = "http://10.20.101.181/auth";

}
