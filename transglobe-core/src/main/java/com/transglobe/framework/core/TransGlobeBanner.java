package com.transglobe.framework.core;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

import java.io.PrintStream;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class TransGlobeBanner implements Banner {

  BannerInfo appTitle = new BannerInfo("spring.application.title", "info.application.title",
      "TransGlobe");
  BannerInfo appName = new BannerInfo("spring.application.name", "info.application.name",
      null);
  BannerInfo appVersion = new BannerInfo("spring.application.version",
      "info.application.version",
      null);

  @Override
  public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
    out.println(
        AnsiOutput.toString(
            AnsiColor.BRIGHT_BLUE,
            "                    *####                       \n" +
                "                 ####,                          \n" +
                "              (####                             \n" +
                "             ####/                              \n" +
                "       #   .####.                               \n" +
                "    %%%    %%%##                                \n" +
                " /%%%     %%%%%/             *###            (((\n" +
                "%%%%%%    %%%%%(   *#########           *(((((((\n" +
                "#%%%%%%%%%%%%%%%%%%%##             ######((  (((\n" +
                "    #%%%%%%%%%%%%            #######         (((\n" +
                "            %%%%%%.                         ##( \n" +
                "             ,%%%%%%                       ###  \n" +
                "                %%%%%%%,                 *##    \n" +
                "                   %%%%%%%%%%*      *           \n" +
                "                        /%%%%%%%##*             \n" +
                "                                                \n",
            AnsiColor.YELLOW,
            Stream.of(appTitle, appName, appVersion)
                .map(info -> info.getValue(environment))
                .filter(Objects::nonNull)
                .collect(joining(" :: "))));

    out.println();
  }

  @AllArgsConstructor
  class BannerInfo {

    String key; // 找 properties 的第一順位
    String fallbackKey; // 第二順位
    String defaultValue; // 預設值

    String getValue(@NonNull Environment environment) {
      return ofNullable(environment.getProperty(key))
          .or(() -> ofNullable(environment.getProperty(fallbackKey)))
          .orElse(defaultValue);
    }
  }
}
