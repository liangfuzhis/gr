package com.gr.security.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gr.security.sso.mapper")
public class GrSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrSsoApplication.class, args);
    }

}
