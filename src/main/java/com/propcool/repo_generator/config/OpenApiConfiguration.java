package com.propcool.repo_generator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Repo Generator Api",
                contact = @Contact(
                        name = "Pavel Kazakov",
                        email = "propcool56@gmail.com"
                )
        )
)
public class OpenApiConfiguration {
}
