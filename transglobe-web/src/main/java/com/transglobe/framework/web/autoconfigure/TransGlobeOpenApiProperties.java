package com.transglobe.framework.web.autoconfigure;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@ConfigurationProperties(prefix = "transglobe.web.openapi")
public class TransGlobeOpenApiProperties {

  String title;
  String description;
  String termsOfService;
  Contact contact;
  License license;
  String version;
  Map<String, Object> extensions = new HashMap<>();
}
