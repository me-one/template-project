package com.megvii.insight.framework.logger.annotation;

import com.megvii.insight.framework.logger.LoggerConfiguration;
import com.megvii.insight.framework.logger.LoggerFormat;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that Logger support should be enabled.
 * <p>
 * This should be applied to a Spring java config and should have an accompanying '@Configuration'
 * annotation.
 * <p>
 * you can override the log message format by creating {@link LoggerFormat} bean.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LoggerConfiguration.class)
public @interface EnableLogger {
}