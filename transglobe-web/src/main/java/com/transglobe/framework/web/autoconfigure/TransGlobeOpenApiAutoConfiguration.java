package com.transglobe.framework.web.autoconfigure;

import static java.util.Optional.ofNullable;

import javax.annotation.PostConstruct;

import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.converters.models.MonetaryAmount;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(TransGlobeOpenApiProperties.class)
public class TransGlobeOpenApiAutoConfiguration {

  private final Environment environment;
  private final TransGlobeOpenApiProperties properties;

  @PostConstruct
  public void init() {
    SpringDocUtils.getConfig()
        .replaceWithSchema(
            MonetaryAmount.class,
            new ObjectSchema()
                .addProperties("amount", new NumberSchema())
                .example(0)
                .addProperties("currency", new StringSchema().example("TWD")));
  }

  // HTTP Bearer, see:
  // https://swagger.io/docs/specification/authentication/bearer-authentication/
  //  @Bean
  //  @ConditionalOnProperty("mimosa.security.enabled")
  //  public OpenAPI securedOpenAPI() {
  //    return new OpenAPI()
  //      .components(
  //        new Components()
  //          .addSecuritySchemes("bearerAuth", new SecurityScheme().type(HTTP).scheme("bearer")))
  //      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
  //      .info(info());
  //  }

  @Bean
  @ConditionalOnMissingBean
  public OpenAPI openAPI() {
    return new OpenAPI().components(new Components()).info(info());
  }

  private Info info() {
    return new Info()
        .description(properties.description)
        .termsOfService(properties.termsOfService)
        .contact(properties.contact)
        .license(properties.license)
        .version(properties.version)
        .title(
            ofNullable(properties.title)
                .filter(StringUtils::hasText)
                .orElseGet(() -> environment.getProperty("spring.application.name") + " API"));
  }
}
