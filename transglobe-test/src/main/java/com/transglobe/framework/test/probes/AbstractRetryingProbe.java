package com.transglobe.framework.test.probes;

import static java.lang.String.format;
import static org.rnorth.ducttape.unreliables.Unreliables.retryUntilTrue;

import java.util.concurrent.TimeUnit;

import org.rnorth.ducttape.TimeoutException;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;

/**
 * @author Matt Ho
 */
public abstract class AbstractRetryingProbe extends AbstractWaitStrategy {

  @Override
  protected void waitUntilReady() {
    long seconds = startupTimeout.getSeconds();
    try {
      retryUntilTrue((int) seconds, TimeUnit.SECONDS,
          () -> getRateLimiter().getWhenReady(this::isReady));
    } catch (TimeoutException e) {
      throw new ContainerLaunchException(
          format(
              "Container[%s] is not ready after [%d] seconds, container cannot be started.",
              waitStrategyTarget.getContainerId(), seconds),
          e);
    }
  }

  protected abstract boolean isReady();
}
