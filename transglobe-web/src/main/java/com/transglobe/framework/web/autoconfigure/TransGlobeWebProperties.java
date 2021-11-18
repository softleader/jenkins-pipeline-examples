package com.transglobe.framework.web.autoconfigure;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.function.BinaryOperator.minBy;
import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.regex.Pattern;

import org.jooq.lambda.function.Consumer3;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.context.request.WebRequest;

import com.transglobe.framework.http.APIError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@ConfigurationProperties(prefix = "transglobe.web")
public class TransGlobeWebProperties {

  WebErrorProperties error = new WebErrorProperties();
  WebJacksonProperties jackson = new WebJacksonProperties();

  @Data
  public static class IncludeStacktrace {

    boolean includeIfHeaderNotExist;
    String headerName = "X-INCLUDE-STACKTRACE";

    public boolean isIncludeStackTrace(@NonNull WebRequest request) {
      return Optional.ofNullable(request.getHeader(headerName))
          .map(Boolean::parseBoolean)
          .orElse(includeIfHeaderNotExist);
    }
  }

  /**
   * 遇到 Error, 要印 log 的挑選等級策略
   */
  @Data
  public static class LoggingStrategy {

    /**
     * 預設的 Log 等級
     */
    @NonNull
    Slf4jLevel defaultLevel = Slf4jLevel.ERROR;

    /**
     * 完全比對 Error code 的等級
     */
    @NonNull
    Map<Slf4jLevel, Collection<String>> exact = new HashMap<>();

    /**
     * 使用 Regex 比對 Error code 的等級
     */
    @NonNull
    @ToString.Exclude
    Map<Slf4jLevel, Collection<Pattern>> regex = new HashMap<>();

    /**
     * 若比對到多個等級, 該如何決定要使用哪一種的策略
     */
    @NonNull
    OverlayStrategy overlayStrategy = OverlayStrategy.HIGHEST;

    /**
     * SLF4J Log 等級
     *
     * @author Matt Ho
     */
    @RequiredArgsConstructor
    public enum Slf4jLevel {
      TRACE(Level.TRACE.toInt(), (log, e, t) -> log.trace("{}", e.shortSummary(), t)),
      DEBUG(Level.DEBUG.toInt(), (log, e, t) -> log.debug("{}", e.shortSummary(), t)),
      INFO(Level.INFO.toInt(), (log, e, t) -> log.info("{}", e.shortSummary(), t)),
      WARN(Level.WARN.toInt(), (log, e, t) -> log.warn("{}", e.shortSummary(), t)),
      ERROR(Level.ERROR.toInt(), (log, e, t) -> log.error("{}", e.shortSummary(), t)),
      OFF(MAX_VALUE, (log, e, t) -> {
      }),
      ;

      @Getter
      final int level;

      @Getter
      @NonNull
      final Consumer3<Logger, APIError, Throwable> logger;
    }

    @RequiredArgsConstructor
    public enum OverlayStrategy {
      HIGHEST(maxBy(comparing(Slf4jLevel::getLevel))),
      LOWEST(minBy(comparing(Slf4jLevel::getLevel))),
      ;

      @NonNull
      @Getter
      final BinaryOperator<Slf4jLevel> operator;
    }
  }

  @Data
  static class WebJacksonProperties {

    Modules modules = new Modules();
  }

  @Data
  static class Modules {

    boolean enabled = true;
  }

  @Data
  class DefaultHandlers {

    boolean enabled = true;
  }

  @Data
  public class WebErrorProperties {

    /**
     * 例外處理器控制相關
     **/
    DefaultHandlers defaultHandlers = new DefaultHandlers();
    /**
     * 例外 trace 控制相關
     **/
    IncludeStacktrace includeStacktrace = new IncludeStacktrace();
    /**
     * 例外 logging 控制相關
     */
    LoggingStrategy loggingStrategy = new LoggingStrategy();
  }
}
