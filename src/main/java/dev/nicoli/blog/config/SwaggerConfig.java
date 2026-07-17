package dev.nicoli.blog.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blog",
                description = "Blog",
                contact = @Contact(
                        name = "Nicoli",
                        email = "nicoli1992@gmail.com"
                ),
                license = @License(
                        name = "License"
                ),
                version = "0.1"
        ),
        externalDocs = @ExternalDocumentation(
                description = "GitHub",
                url = "https://github.com/nic0li"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

}
