package com.transglobe.framework.test.autoconfigure.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.shaded.org.apache.commons.lang.exception.ExceptionUtils;
import org.testcontainers.utility.DockerImageName;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis Container 設定
 *
 * @author Matt Ho
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisContainerProperties.class)
public class RedisContainerAutoConfiguration {

  @Getter
  private static volatile GenericContainer redis;

  final RedisContainerProperties properties;

  @Synchronized
  @PostConstruct
  public void setup() {
    if (redis != null && redis.isRunning()) {
      log.trace("skip starting redis server because it is already running...");
      return;
    }
    try {
      redis = new GenericContainer(DockerImageName.parse(properties.getImageName()))
          .withExposedPorts(properties.getPort())
          .withLogConsumer(new Slf4jLogConsumer(log))
          .waitingFor(
              Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));
      redis.start();
      log.info("Successfully started redis container on {}:{}", redis.getContainerIpAddress(),
          redis.getExposedPorts());
    } catch (Exception e) {
      log.warn("Failed to start embedded redis server: {}", ExceptionUtils.getRootCauseMessage(e));
      if (properties.isFastFail()) {
        throw e;
      }
    }
  }

  @Synchronized
  @PreDestroy
  public void destroy() {
    if (redis != null) {
      redis.stop();
    }
  }

}
