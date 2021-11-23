package com.transglobe.framework.web.servlet;

import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.lang.Nullable;

import com.transglobe.framework.http.APIError;
import com.transglobe.framework.web.autoconfigure.TransGlobeWebProperties.WebErrorProperties.LoggingStrategy;
import com.transglobe.framework.web.autoconfigure.TransGlobeWebProperties.WebErrorProperties.LoggingStrategy.Slf4jLevel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 可設定等級的 error logging
 *
 * @author Matt Ho
 */
@Slf4j
@RequiredArgsConstructor
class APIErrorLogger {

  @NonNull
  final LoggingStrategy strategy;
  @NonNull
  final Logger logger;

  void log(@NonNull APIError error, @Nullable Throwable t) {
    var level = findLevel(error.getCode(), t);
    log.trace("logging message for level: {}", level);
    level.getLogger().accept(logger, error, t);
  }

  Slf4jLevel findLevel(@NonNull String code, @Nullable Throwable t) {
    return findExactLevel(code) // 找 exact 的設定
        .or(() -> findRegexLevel(code)) // 找 regex 的設定
        .orElse(strategy.getDefaultLevel());
  }

  Optional<Slf4jLevel> findExactLevel(@NonNull String code) {
    return strategy.getExact().entrySet().stream()
        .filter(entry -> entry.getValue().stream().anyMatch(code::equals))
        .map(Entry::getKey)
        .reduce(strategy.getOverlayStrategy().getOperator());
  }

  Optional<Slf4jLevel> findRegexLevel(@NonNull String code) {
    return strategy.getRegex().entrySet().stream()
        .filter(entry -> entry.getValue().stream().anyMatch(p -> p.matcher(code).matches()))
        .map(Entry::getKey)
        .reduce(strategy.getOverlayStrategy().getOperator());
  }
}
