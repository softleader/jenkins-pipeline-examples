package com.transglobe.framework.http;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表在 REST API 的呼叫過程中, 被 AOP 抓到的 Throwable
 *
 * @author Matt Ho
 */
@Getter
@ToString
public class ExceptionCaughtEvent<T extends Throwable> extends ApplicationEvent implements
    ResolvableTypeProvider {

  final APIError error;

  @Builder
  ExceptionCaughtEvent(@NonNull T throwable, @NonNull APIError error) {
    super(throwable);
    this.error = error;
  }

  /**
   * @return 捕捉到的時間
   */
  public final LocalDateTime getCaughtTime() {
    return ofInstant(ofEpochMilli(getTimestamp()), systemDefault());
  }

  @Override
  public ResolvableType getResolvableType() {
    return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(source));
  }
}
