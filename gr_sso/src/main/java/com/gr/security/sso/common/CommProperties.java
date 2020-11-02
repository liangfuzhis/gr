package com.gr.security.sso.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取Properties文件
 */
@PropertySource({"classpath:/base/sys.properties"})
@Component
public class CommProperties {

    @Value("${system.client.clientId}")
    private @Getter @Setter String clientId;

    @Value("${system.client.clientSecret}")
    private @Getter @Setter String clientSecret;
}
