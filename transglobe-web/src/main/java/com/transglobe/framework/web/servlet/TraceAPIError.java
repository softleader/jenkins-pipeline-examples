package com.transglobe.framework.web.servlet;

import static lombok.AccessLevel.PACKAGE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.lang.Nullable;

import com.transglobe.framework.http.APIError;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * @author Matt Ho
 */
@Schema(description = "錯誤結構 with traces")
@Data
@SuperBuilder
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor
class TraceAPIError extends APIError {

  @ArraySchema(schema = @Schema(description = "Root cause stacktrace", nullable = true))
  List<String> trace;

  TraceAPIError(@NonNull APIError error, @Nullable Throwable ex) {
    super.status = error.getStatus();
    super.code = error.getCode();
    super.message = error.getMessage();
    super.details = error.getDetails();
    this.trace = Optional.ofNullable(ex)
        .map(ExceptionUtils::getRootCauseStackTrace)
        .map(Arrays::asList)
        .filter(Predicate.not(List::isEmpty))
        .orElse(null);
  }
}
