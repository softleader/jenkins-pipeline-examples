package com.transglobe.framework.test.wait.strategy;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.rnorth.ducttape.unreliables.Unreliables.retryUntilTrue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.rnorth.ducttape.TimeoutException;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Exec command wait strategy
 *
 * @author Matt Ho
 */
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ExecWaitStrategy extends AbstractWaitStrategy {

  public static final int DEFAULT_EXIT_CODE = 0;

  @NonNull
  final String[] command;
  int exitCode = DEFAULT_EXIT_CODE;

  public static ExecWaitStrategy execCommand(String... command) {
    return new ExecWaitStrategy(command);
  }

  @Override
  protected void waitUntilReady() {
    long seconds = startupTimeout.getSeconds();
    try {
      retryUntilTrue((int) seconds, TimeUnit.SECONDS,
          () -> getRateLimiter().getWhenReady(this::commandSucceeded));
    } catch (TimeoutException e) {
      throw new ContainerLaunchException(
          format(
              "Container[%s] is not ready after [%d] seconds, container cannot be started.",
              waitStrategyTarget.getContainerId(), seconds),
          e);
    }
  }

  @SneakyThrows
  private boolean commandSucceeded() {
    log.debug("execution of command {} for container id: {} ",
        Arrays.toString(command), waitStrategyTarget.getContainerId());
    var result = waitStrategyTarget.execInContainer(command);
    log.debug("executed with result: {}", result);
    return result.getExitCode() == exitCode;
  }

  public ExecWaitStrategy withExitCode(int exitCode) {
    this.exitCode = exitCode;
    return this;
  }
}
