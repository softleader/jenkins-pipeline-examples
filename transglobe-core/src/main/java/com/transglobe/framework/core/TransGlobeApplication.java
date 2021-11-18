package com.transglobe.framework.core;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public final class TransGlobeApplication {

  private static final String PID_PATH = "transglobe.pid.path";

  public static ConfigurableApplicationContext run(Class<?> source, String[] args) {
    return new SpringApplicationBuilder()
        .sources(source)
        .banner(new TransGlobeBanner())
        .listeners(new ApplicationPidFileWriter(pid()))
        .build().run(args);
  }

  private static String pid() {
    return Path.of(System.getProperty(PID_PATH, "./bin"), format("app%s.pid", hostname()))
        .toString();
  }

  private static String hostname() {
    try {
      return "-" + InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "";
    }
  }

}
