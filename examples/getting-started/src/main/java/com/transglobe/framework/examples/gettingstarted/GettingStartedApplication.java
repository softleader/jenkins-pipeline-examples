package com.transglobe.framework.examples.gettingstarted;

import static java.lang.String.format;

import com.transglobe.framework.core.TransGlobeApplication;
import com.transglobe.framework.core.TransGlobeBootstrap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@TransGlobeBootstrap
public class GettingStartedApplication {

  public static void main(String[] args) {
    TransGlobeApplication.run(GettingStartedApplication.class, args);
  }
}

@Tag(name = "嗨")
@Slf4j
@RestController
@RequestMapping("/greeting")
class GreetingController {

  @Operation(description = "打聲招呼")
  @GetMapping("/{name}")
  public String greeting(
      @Parameter(description = "你的名字") @PathVariable("name") String name) {
    log.info("Hello, {}!", name);
    return format("Hello, %s!", name);
  }
}
