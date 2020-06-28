package br.com.mercadolivre.proxy.config;

import br.com.mercadolivre.proxy.controller.ParameterController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan(basePackageClasses = ParameterController.class)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .select()
                .apis( RequestHandlerSelectors.basePackage( "br.com.mercadolivre.proxy.controller" ))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("API with parameter of Meli Proxy")
                .title("PROXY API for Meli")
                .version("3.0.0")
                .build();
    }

}
