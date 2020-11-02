package com.gr.security.sso.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("system.client")
public class ClientSystem {
    private ClientProperties [] clients = {};
}
