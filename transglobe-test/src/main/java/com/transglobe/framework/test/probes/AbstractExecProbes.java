package com.transglobe.framework.test.probes;

import java.util.Arrays;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Matt Ho
 */
@Slf4j
public abstract class AbstractExecProbes extends AbstractRetryingProbe {

  public static AbstractExecProbes execCommand(String... commands) {
    return new AbstractExecProbes() {
      @Override
      public String[] getCommand() {
        return commands;
      }
    };
  }

  @SneakyThrows
  @Override
  protected boolean isReady() {
    var command = getCommand();
    log.debug("execution of command {} for container id: {} ",
        Arrays.toString(command), waitStrategyTarget.getContainerId());
    var result = waitStrategyTarget.execInContainer(command);
    log.debug("executed with result: {}", result);
    return result.getExitCode() == 0;
  }

  protected abstract String[] getCommand();
}
