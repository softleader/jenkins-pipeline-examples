package com.transglobe.framework.test.autoconfigure.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "transglobe.test.redis")
class RedisContainerProperties {

  String imageName;
  Integer port;
  boolean fastFail;
}
