package com.transglobe.framework.data.jpa.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.transglobe.framework.data.jpa.HikariLog4jdbcBeanPostProcessor;
import com.transglobe.framework.data.jpa.Log4jdbcBeanPostProcessor;
import com.transglobe.framework.data.jpa.SpringSecurityAuditorAware;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataSourceSpy.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(TransGlobeDataJpaProperties.class)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaAuditing
public class TransGlobeDataJpaAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public AuditorAware<String> springSecurityAuditorAware() {
    return new SpringSecurityAuditorAware();
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnProperty(value = "mimosa.data.jpa.log4jdbc.enabled", matchIfMissing = true)
  public static class Log4jdbcConfiguration {

    @Bean
    @ConditionalOnMissingClass("com.zaxxer.hikari.HikariDataSource")
    @ConditionalOnMissingBean(Log4jdbcBeanPostProcessor.class)
    public Log4jdbcBeanPostProcessor log4jdbcBeanPostProcessor() {
      return new Log4jdbcBeanPostProcessor();
    }

    @Bean
    @ConditionalOnClass(HikariDataSource.class)
    @ConditionalOnMissingBean(HikariLog4jdbcBeanPostProcessor.class)
    public HikariLog4jdbcBeanPostProcessor hikariLog4jdbcBeanPostProcessor() {
      return new HikariLog4jdbcBeanPostProcessor();
    }
  }
}
