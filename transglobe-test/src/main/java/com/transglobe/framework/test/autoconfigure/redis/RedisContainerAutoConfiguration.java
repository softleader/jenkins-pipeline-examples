package com.transglobe.framework.test.autoconfigure.redis;

import static com.transglobe.framework.test.probes.AbstractExecProbes.execCommand;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
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
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisContainerAutoConfiguration {

  @Getter
  private static volatile GenericContainer redis;
  private static final int REDIS_DEFAULT_PORT = 6379;

  final Optional<RedisProperties> redisProperties;
  final RedisContainerProperties redisContainerProperties;

  @Synchronized
  @PostConstruct
  public void setup() {
    if (redis != null && redis.isRunning()) {
      log.trace("skip starting redis server because it is already running...");
      return;
    }
    try {
      var port = redisProperties.map(RedisProperties::getPort).orElse(REDIS_DEFAULT_PORT);
      redis = new GenericContainer(DockerImageName.parse(redisContainerProperties.getImageName()))
          .withExposedPorts(port)
          .withLogConsumer(new Slf4jLogConsumer(log))
          .waitingFor(execCommand("redis-cli", "-p", String.valueOf(port), "ping"));
      redis.start();
      log.info("Successfully started redis container on {}:{}", redis.getContainerIpAddress(),
          redis.getExposedPorts());
    } catch (Exception e) {
      log.warn("Failed to start redis container: {}", ExceptionUtils.getRootCauseMessage(e));
      if (redisContainerProperties.isFastFail()) {
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
