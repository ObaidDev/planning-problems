package com.trackswiftly.vehicle_routing.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class GeneralConfig {


    @Value("${swagger.server-urls}")
    private List<String> serverUrls;




    @SuppressWarnings("unchecked")
    @Bean
    public OpenAPI customOpenAPI() {
    
        List<Server> servers = serverUrls.stream()
                .map(url -> new Server().url(url))
                .toList();
        
        return new OpenAPI()
                .info(new Info().title("Vehicle Routing Planner API")
                        .description("This API provides services for managing vehicle routing, including assigning visits to vehicles and tracking routes.")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Vehicle Routing Planner Wiki Documentatio")
                        .url("https://springboot.wiki.github.org/docs"))
                .components(
                        new Components()
                                .addRequestBodies("FileUploadRequest",
                                new RequestBody()
                                .content(new Content()
                                        .addMediaType("multipart/form-data",
                                        new MediaType()
                                                .schema(new Schema<>()
                                                .type("array")
                                                .items(new Schema<>()
                                                        .type("string")
                                                        .format("binary"))))))
                )
                .servers(servers);
    }
    
}
