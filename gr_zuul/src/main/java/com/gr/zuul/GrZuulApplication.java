package com.gr.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class GrZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrZuulApplication.class, args);
    }

}
