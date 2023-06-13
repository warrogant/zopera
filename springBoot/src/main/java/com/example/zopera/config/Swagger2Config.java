package com.example.zopera.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration          //告诉Spring容器，这个类是一个配置类
@EnableSwagger2   // 启用Swagger2功能
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
//                com.example.zopera包下所有API都交给Swagger2管理
                .apis(RequestHandlerSelectors.basePackage("com.example.zopera"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API文档页面显示信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ZOPERA RESTful APIs")      //标题
                .description("ZOPERA Swagger2 ")       //描述
                .version("1.0")
                .build();
    }
}