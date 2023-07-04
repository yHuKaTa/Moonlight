package com.aacdemy.moonlight.config.openapi;

import com.aacdemy.moonlight.util.ReadJsonFileToJsonObject;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.io.IOException;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    ReadJsonFileToJsonObject readJsonFileToJsonObject = new ReadJsonFileToJsonObject();

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        io.swagger.v3.oas.models.responses.ApiResponse badRequestAPI = new io.swagger.v3.oas.models.responses.ApiResponse()
                .content(new io.swagger.v3.oas.models.media.Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example()
                                        .value(readJsonFileToJsonObject.read().get("badRequestResponse").toString()))))
                .description("Bad Request");
        io.swagger.v3.oas.models.responses.ApiResponse internalServerErrorResponseAPI = new io.swagger.v3.oas.models.responses.ApiResponse().content(
                        new io.swagger.v3.oas.models.media.Content()
                                .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                                new Example()
                                                        .value(readJsonFileToJsonObject.read().get("internalServerErrorResponse").toString()))))
                .description("Internal Server Error");
        io.swagger.v3.oas.models.responses.ApiResponse notFoundResponseAPI = new io.swagger.v3.oas.models.responses.ApiResponse()
                .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                        new Example()
                                                .value(readJsonFileToJsonObject.read().get("notFoundResponse").toString()))))
                .description("Not Found");
        Components components = new Components();
        components.addResponses("badRequestAPI", badRequestAPI);
        components.addResponses("internalServerErrorResponseAPI", internalServerErrorResponseAPI);
        components.addResponses("notFoundResponseAPI", notFoundResponseAPI);
        return new OpenAPI()
                .info(new Info().title("Moonlight & Spa Resort").version("1.0.0"))
                .components(new Components())
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .externalDocs(new ExternalDocumentation()
                        .description("Moonlight & Spa Resort documentation")
                        .url("https://bit.ly/rent-a-car-sunny"));
    }
}
