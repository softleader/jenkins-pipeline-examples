package com.transglobe.framework.test.autoconfigure.redis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;

/**
 * 自動配置 Redis Testcontainer
 *
 * @author Matt Ho
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
public @interface AutoConfigureRedisContainer {

  @PropertyMapping("transglobe.test.redis.image-name")
  String imageName() default "redis:6.2-alpine";

  @PropertyMapping("transglobe.test.redis.fast-fail")
  boolean fastFail() default true;

  @PropertyMapping("transglobe.test.redis.port")
  int port() default 6379;
}
