package com.gr.security.sso.config.swagger;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2初始化
 * @anthor lfz
 * @since  2020.09.26
 * 访问地址
 * http://localhost:9012/swagger-ui.html 老版本
 * http://localhost:9012/doc.html 新版本
 * 配置security
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.gr.security.sso.controller";

    /**
     * 控制开启或关闭swagger
     */
    @Value("${swagger.enabled}")
    private boolean swaggerEnabled;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // api基础信息
                .apiInfo(apiInfo())
                // 控制开启或关闭swagger
                .enable(swaggerEnabled)
                // 选择那些路径和api会生成document
                .select()
                // 扫描展示api的路径包
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                // 对所有路径进行监控
                .paths(PathSelectors.any())
                // 构建
                .build();
    }

    /**
     * @descripiton:
     * @author: lfz
     * @date: 2020.09.26
     * @param
     * @exception：
     * @modifier：
     * @return：springfox.documentation.service.ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // api名称
                .title("单点登陆服务-接口")
                // api 描述
                .description("SwaggerUI2 API")
                .termsOfServiceUrl("http://localhost:9012/doc.html")
                // api 版本
                .version("2.9.2")
                // 构建
                .build();
    }
}
