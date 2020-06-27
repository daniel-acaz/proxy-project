package br.com.mercadolivre.statistic.config;

import br.com.mercadolivre.statistic.controller.RequestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan(basePackageClasses = RequestController.class)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .select()
                .apis( RequestHandlerSelectors.basePackage( "br.com.mercadolivre.statistic.controller" ))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("API to analysis and statistics of Meli requests")
                .title("STATISTIC API for Meli")
                .version("3.0.0")
                .build();
    }
}
