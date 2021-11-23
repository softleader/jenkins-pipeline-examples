package com.transglobe.framework.examples.loggingjson;

import com.transglobe.framework.core.TransGlobeApplication;
import com.transglobe.framework.core.TransGlobeBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** @author Matt Ho */
@TransGlobeBootstrap
public class LoggingJsonApplication {

  public static void main(String[] args) {
    TransGlobeApplication.run(LoggingJsonApplication.class, args);
  }

  @Slf4j
  @Component
  static class WriteSomeLog implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
      log.info("this is my log message"); // 查看 console
    }
  }
}
