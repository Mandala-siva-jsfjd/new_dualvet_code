 package com.strive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@OpenAPIDefinition(info = @Info(title ="DualVet REST API ",description = "DUALVET REST API Documentation",version = "V1",
contact = @Contact(url = "https://dualvet-dev.tatastrive.com/dualvet/swagger-ui/index.html"),
license = @License(name = "Apache Tomcat 10.1.25")))
@Configuration
public class DualvetDetails extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DualvetDetails.class, args);
        System.out.println("Dualvet Service Starting");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DualvetDetails.class);
    }
}
