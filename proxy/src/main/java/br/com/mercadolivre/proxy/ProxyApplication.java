package br.com.mercadolivre.proxy;

import br.com.mercadolivre.proxy.service.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableRabbit
@EnableSwagger2
@Slf4j
public class ProxyApplication {

    public static void main(String[] args) {

        log.info("[STARTUP] application starting...");

        ConfigurableApplicationContext context = SpringApplication.run(ProxyApplication.class, args);

        context.getBean(ParameterService.class).createSingletonRegistryParameter();
    }

}
