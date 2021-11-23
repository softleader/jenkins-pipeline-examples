package com.transglobe.framework.web.autoconfigure;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.jackson.datatype.money.MoneyModule;

import com.transglobe.framework.http.APIErrorExceptionHandler;
import com.transglobe.framework.web.filter.TraceContextInResponseFilter;
import com.transglobe.framework.web.servlet.DefaultAPIErrorExceptionHandlers;
import com.transglobe.framework.web.servlet.GlobalRestControllerExceptionHandler;

import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(TransGlobeWebProperties.class)
public class TransGlobeWebAutoConfiguration {

  final TransGlobeWebProperties properties;

  @Bean
  @ConditionalOnMissingBean(ResponseEntityExceptionHandler.class)
  GlobalRestControllerExceptionHandler globalRestControllerExceptionHandler(
      ServerProperties serverProperties,
      List<APIErrorExceptionHandler> handlers) {
    return new GlobalRestControllerExceptionHandler(
        serverProperties.getError(),
        properties.getError(),
        handlers) {
    };
  }

  @Bean
  @ConditionalOnClass({ Tracer.class })
  @ConditionalOnProperty(value = "spring.sleuth.enabled", matchIfMissing = true)
  Filter traceIdInResponseFilter(Tracer tracer) {
    return new TraceContextInResponseFilter(tracer, properties.getTrace().getResponseHeaderName());
  }

  @Configuration
  @ConditionalOnProperty(value = "transglobe.web.error.default-handlers.enabled", matchIfMissing = true)
  @Import(DefaultAPIErrorExceptionHandlers.class)
  static class EnabledDefaultErrorHandlers {

  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(JpaRepositoriesAutoConfiguration.class)
  SpecificationArgumentResolver specificationArgumentResolver() {
    return new SpecificationArgumentResolver();
  }

  @Configuration
  @ConditionalOnProperty(value = "transglobe.web.jackson.modules.enabled", matchIfMissing = true)
  static class RegisterJacksonModules {
    @Bean
    @ConditionalOnMissingBean
    MoneyModule moneyModule() {
      return new MoneyModule();
    }
  }
}
