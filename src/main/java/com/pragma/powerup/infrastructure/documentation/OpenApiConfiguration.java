package com.pragma.powerup.infrastructure.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi(
            @Value("${appDescription}") String appDescription,
            @Value("${appVersion}") String appVersion) {
        
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API de Usuarios - Plaza de Comidas")
                        .version(appVersion)
                        .description(appDescription)
                        .contact(new Contact()
                                .name("Pragma - Plaza de Comidas")
                                .email("soporte@plazacomidas.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo")
                ));
    }
}
