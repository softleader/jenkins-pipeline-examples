package com.transglobe.framework.web;

import static com.transglobe.framework.web.DefaultAPIErrorExceptionHandlers.UNEXPECTED_ERROR_HANDLER;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.transglobe.framework.http.APIErrorExceptionHandler;
import com.transglobe.framework.http.ExceptionCaughtEvent;
import com.transglobe.framework.web.autoconfigure.TransGlobeWebProperties.WebErrorProperties;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 核心預設的全域 Exception Handler
 *
 * @author Matt Ho
 */
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public abstract class GlobalRestControllerExceptionHandler extends
    ResponseEntityExceptionHandler implements
    ApplicationEventPublisherAware, InitializingBean {

  final ErrorProperties errorProperties;
  final WebErrorProperties webErrorProperties;
  final List<APIErrorExceptionHandler> handlers;

  APIErrorLogger apiErrorLogger;

  @Setter
  ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void afterPropertiesSet() {
    this.apiErrorLogger = new APIErrorLogger(webErrorProperties.getLoggingStrategy(), log);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      @Nullable Exception ex, @Nullable Object body, @NonNull HttpHeaders headers,
      @NonNull HttpStatus status, @NonNull WebRequest request) {
    // 如果有 body 就不多做額外的處理了 (理論上 parent 抓到的例外都不會有 body)
    if (body != null) {
      return super.handleExceptionInternal(ex, body, headers, status, request);
    }
    var handler = findBestCandidateHandler(handlers, ex).orElse(UNEXPECTED_ERROR_HANDLER);
    log.trace("found best candidate handler [{}] to handle: {}", handler, ex);
    var error = requireNonNull(handler.buildError(ex, request),
        "Built APIError must not returns null but is null");
    // 如果該 handler 所產出的 error 有指定 status 就優先使用
    if (error.getStatus() != null) {
      status = error.getStatus();
    }
    log.trace(
        "building status=[{}], api-error=[{}], exception=[{}] to error-response",
        status,
        error.shortSummary(),
        ExceptionUtils.getMessage(ex));
    if (includeStackTrace(request)) {
      error = new TraceAPIError(error, ex);
    }
    if (ex != null) {
      var event = ExceptionCaughtEvent.builder().throwable(ex).error(error).build();
      log.trace("publishing error-caught-event for {}", event);
      applicationEventPublisher.publishEvent(event);
    }
    apiErrorLogger.log(error, ex);
    return super.handleExceptionInternal(ex, error, headers, status, request);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> defaultExceptionHandler(Exception ex, WebRequest request) {
    return handleExceptionInternal(ex, null, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
  }

  /**
   * 找出處理 {@code ex} 的 {@link APIErrorExceptionHandler} 的最佳解
   *
   * <p>
   * 專案想改變這邏輯可以實作此 class override 此 method
   * </p>
   *
   * @return {@code Optional.empty()} if found no one
   */
  protected Optional<APIErrorExceptionHandler> findBestCandidateHandler(
      @NonNull List<APIErrorExceptionHandler> handlers, @Nullable Exception ex) {
    return handlers.stream()
        .filter(handler -> handler.supports(ex))
        .findFirst();// 先找到先用, 因此 handlers 所註冊在 spring 的順序是重要的
  }

  boolean includeStackTrace(WebRequest request) {
    return errorProperties.getIncludeStacktrace() == IncludeAttribute.ALWAYS
        || webErrorProperties.getIncludeStacktrace().isIncludeStackTrace(request);
  }
}
