package io.neond.genesis;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi rootOpenApi() {
        return GroupedOpenApi.builder()
                .group("root")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberOpenApi() {
        return GroupedOpenApi.builder()
                .group("member")
                .pathsToMatch("/member/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminOpenApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi testOpenApi() {
        return GroupedOpenApi.builder()
                .group("test")
                .pathsToMatch("/test/**")
                .build();
    }
}
