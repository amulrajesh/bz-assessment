package com.ar.bzassesment.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openApiConfiguration() {
        Server localServer = new Server()
                .url("http://localhost:8080/")
                .description("Recipe Manager");

        Contact contact = new Contact().email("rrajesh.try@gmail.com").name("Rajesh Ravi");

        Info info = new Info()
                .contact(contact)
                .summary("Application to manage recipes.")
                .title("Recipe Manager")
                .version("1.0")
                .description("Application to manage recipe, add new recipe, update existing recipe, delete and search recipe using filter logic")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI().info(info).addServersItem(localServer);
    }
}

