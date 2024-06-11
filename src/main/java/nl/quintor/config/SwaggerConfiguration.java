package nl.quintor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI MusiQOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        return openAPI
                .info(new Info().title("Favourite recipe manager")
                        .description("manage your favourite recipes")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://quintor.nl")));
    }

}
