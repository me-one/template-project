package com.megvii.insight.framework.logger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class LoggerConfiguration {

  @Bean
  @ConditionalOnMissingBean(LoggerFormat.class)
  public LoggerFormat loggerFormat() {
    return LoggerFormat.builder()
        .enter("#${method.name}(${method.args}): "
            + "entered")
        .warnBefore("#${method.name}(${method.args}): "
            + "in ${method.duration} and still running (max ${method.warn.duration})")
        .warnAfter("#${method.name}(${method.args}): "
            + "${method.result} in ${method.duration} (max ${method.warn.duration})")
        .after("#${method.name}(${method.args}): "
            + "${method.result} in ${method.duration}")
        .error("#${method.name}(${method.args}): "
            + "thrown ${error.class.name}(${error.message}) "
            + "from ${error.source.class.name}[${error.source.line}] in ${method.duration} "
            + "\n${error.stacktrace}")
        .build();
  }
}