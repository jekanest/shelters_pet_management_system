package com.springboot.project.shelterpet.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Config {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.springboot.project.shelterpet"))
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return docket;
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.SHELTERSPET,
                        "Controller used to get, create, update and delete shelters pets data")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Shelters pets management systems API")
                .description("Simple shelters pets management systems API")
                .version("1.0")
                .build();
    }
}