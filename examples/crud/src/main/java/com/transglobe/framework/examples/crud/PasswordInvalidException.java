package com.transglobe.framework.examples.crud;

import com.transglobe.framework.http.APIError;
import com.transglobe.framework.http.APIErrorDetail;
import com.transglobe.framework.http.APIErrorExceptionHandler;
import com.transglobe.framework.http.ExceptionCaughtEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

public class PasswordInvalidException extends RuntimeException {

  public PasswordInvalidException(String message) {
    super(message);
  }
}

@Component
class PasswordInvalidExceptionHandler implements APIErrorExceptionHandler {

  @Override
  public boolean supports(Exception exception) {
    return exception instanceof PasswordInvalidException;
  }

  @Override
  public APIError buildError(Exception exception,
      WebRequest request) {
    return APIError.builder()
        .code("E111") // TODO, 先隨便寫個 code 及 message
        .message("password invalid")
        .detail(APIErrorDetail.builder()
            .code("E111_1?")
            .message(exception.getMessage())
            .build())
        .build();
  }
}

@Slf4j
@Component
class PasswordInvalidExceptionListener {

  @Async
  @EventListener
  public void onException(ExceptionCaughtEvent<PasswordInvalidException> event) {
    log.warn("error=[{}], time=[{}]", event.getError().shortSummary(), event.getCaughtTime());
  }
}
