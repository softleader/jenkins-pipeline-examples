package com.transglobe.framework.data.jpa.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "transglobe.data.jpa")
public class TransGlobeDataJpaProperties {

  private Log4jdbc log4jdbc = new Log4jdbc();

  @Data
  public static class Log4jdbc {

    private boolean enabled = true;
  }

}
