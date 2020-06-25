package br.com.mercadolivre.proxy;

import br.com.mercadolivre.proxy.service.ParameterService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableZuulProxy
@EnableRabbit
public class ProxyApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ProxyApplication.class, args);

        context.getBean(ParameterService.class).createSingletonRegistryParameter();
    }

}
