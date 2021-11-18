package com.transglobe.framework.web;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;

import com.transglobe.framework.http.APIError;
import com.transglobe.framework.http.APIErrorDetail;
import com.transglobe.framework.http.APIErrorDetail.APIErrorDetailBuilder;
import com.transglobe.framework.http.APIErrorExceptionHandler;

import lombok.NonNull;

/**
 * 核心所提供的預設 Exception 轉換 {@link APIError} 實作
 *
 * @author Matt Ho
 */
public class DefaultAPIErrorExceptionHandlers {

  static final APIErrorExceptionHandler UNEXPECTED_ERROR_HANDLER = APIErrorExceptionHandler.builder()
      .supports(ex -> true)
      .buildError((ex, req) -> buildErrorWithExceptionMessage("E999", "Unexpected error", ex))
      .build();

  private static APIError buildErrorWithExceptionMessage(@NonNull String code,
      @NonNull String message,
      @NonNull Exception ex) {
    return APIError.builder()
        .code(code)
        .message(message)
        .details(ofNullable(ex.getMessage())
            .map(APIErrorDetail.builder()::message)
            .map(APIErrorDetailBuilder::build)
            .map(List::of)
            .orElseGet(Collections::emptyList))
        .build();
  }

  @Order
  @Bean
  APIErrorExceptionHandler illegalArgumentExceptionHandler() {
    return APIErrorExceptionHandler.builder()
        .supports(IllegalArgumentException.class::isInstance)
        .buildError((ex, req) -> buildErrorWithExceptionMessage("E001", "Illegal argument", ex))
        .build();
  }

  @Order
  @Bean
  APIErrorExceptionHandler illegalStateExceptionHandler() {
    return APIErrorExceptionHandler.builder()
        .supports(IllegalArgumentException.class::isInstance)
        .buildError((ex, req) -> buildErrorWithExceptionMessage("E002", "Illegal state", ex))
        .build();
  }

  @Order
  @Bean
  APIErrorExceptionHandler unsupportedOperationExceptionHandler() {
    return APIErrorExceptionHandler.builder()
        .supports(UnsupportedOperationException.class::isInstance)
        .buildError(
            (ex, req) -> buildErrorWithExceptionMessage("E003", "Unsupported operation", ex))
        .build();
  }

  @Order
  @Bean
  APIErrorExceptionHandler bindExceptionHandler() {
    return APIErrorExceptionHandler.builder()
        .supports(BindException.class::isInstance)
        .buildError((ex, req) -> APIError.builder()
            .code("E004")
            .message("Binding error")
            .details(((BindException) ex).getAllErrors().stream().map(APIValidationError::new)
                .collect(toList()))
            .build())
        .build();
  }

  @Order
  @Bean
  APIErrorExceptionHandler httpMessageNotReadableExceptionHandler() {
    return APIErrorExceptionHandler.builder()
        .supports(HttpMessageNotReadableException.class::isInstance)
        .buildError(
            (ex, req) -> buildErrorWithExceptionMessage("E005", "Http message not readable",
                ex))
        .build();
  }
}
