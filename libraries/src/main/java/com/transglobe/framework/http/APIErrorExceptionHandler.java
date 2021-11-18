package com.transglobe.framework.http;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;

import lombok.NonNull;

/**
 * 將 Runtime 例外封裝成 {@code ProblemDetail} 的處理器.
 * <p>
 *
 * 在 {@code RestExceptionHandler} 中已經處理許多共通的例外, 當在 App 在實作上若還有更多的例外要捕捉, 則可透過實作這隻界面來擴充處理邏輯.
 *
 * @author Matt Ho
 */
public interface APIErrorExceptionHandler {

  static APIErrorExceptionHandlerBuilder builder() {
    return new APIErrorExceptionHandlerBuilder();
  }

  /**
   * 判斷此 handler 可處理傳入的例外, 若有多個 handler 符合則挑選第一個符合的做處理.
   * <p>
   *
   * 實作上可透過標記 {@code @Order} 在 class level 來控制順序
   *
   * @see Order
   */
  boolean supports(@Nullable Exception exception);

  /**
   * 將 {@link Exception} 及 {@link WebRequest} 處理成一個 {@link APIError} 的實作
   *
   * @throws NullPointerException if returns null
   */
  APIError buildError(Exception exception, WebRequest request);

  class APIErrorExceptionHandlerBuilder {

    Predicate<Exception> supports;
    BiFunction<Exception, WebRequest, APIError> buildError;

    public APIErrorExceptionHandlerBuilder supports(@NonNull Predicate<Exception> supports) {
      this.supports = supports;
      return this;
    }

    public APIErrorExceptionHandlerBuilder buildError(
        @NonNull BiFunction<Exception, WebRequest, APIError> buildError) {
      this.buildError = buildError;
      return this;
    }

    public APIErrorExceptionHandler build() {
      requireNonNull(supports, "Requires 'supports' but is null");
      requireNonNull(buildError, "Requires 'buildError' but is null");
      return new APIErrorExceptionHandler() {
        @Override
        public boolean supports(Exception exception) {
          return supports.test(exception);
        }

        @Override
        public APIError buildError(Exception exception,
            WebRequest request) {
          return buildError.apply(exception, request);
        }
      };
    }
  }
}
