package com.megvii.insight.framework.logger;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.megvii.insight.framework.logger.annotation.Loggable;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Helper class for log message format.
 */
final class LoggerFormatter {

  private static final String DOTS = "..";

  private static final String NAME_KEY = "method.name";
  private static final String ARGS_KEY = "method.args";
  private static final String RESULT_KEY = "method.result";
  private static final String DURATION_KEY = "method.duration";
  private static final String WARN_DURATION_KEY = "method.warn.duration";
  private static final String ERROR_CLASS_KEY = "error.class.name";
  private static final String ERROR_MSG_KEY = "error.message";
  private static final String ERROR_SOURCE_CLASS_KEY = "error.source.class.name";
  private static final String ERROR_LINE_KEY = "error.source.line";

  private LoggerFormat format;

  public LoggerFormatter(LoggerFormat format) {
    this.format = format;
  }

  public String enter(ProceedingJoinPoint joinPoint, Loggable loggable) {
    Map<String, Object> values = new HashMap<>();
    values.put(NAME_KEY, methodName(joinPoint));
    values.put(ARGS_KEY, methodArgs(joinPoint, loggable));
    return StrSubstitutor.replace(format.getEnter(), values);
  }

  public String warnBefore(ProceedingJoinPoint joinPoint, Loggable loggable, long nano) {
    Map<String, Object> values = new HashMap<>();
    values.put(NAME_KEY, methodName(joinPoint));
    values.put(ARGS_KEY, methodArgs(joinPoint, loggable));
    values.put(DURATION_KEY, durationString(nano));
    values.put(WARN_DURATION_KEY, warnDuration(loggable));
    return StrSubstitutor.replace(format.getWarnBefore(), values);
  }

  public String warnAfter(ProceedingJoinPoint joinPoint, Loggable loggable, Object result, long nano) {
    Map<String, Object> values = new HashMap<>();
    values.put(NAME_KEY, methodName(joinPoint));
    values.put(ARGS_KEY, methodArgs(joinPoint, loggable));
    values.put(DURATION_KEY, durationString(nano));
    values.put(WARN_DURATION_KEY, warnDuration(loggable));
    values.put(RESULT_KEY, methodResults(result, loggable));
    return StrSubstitutor.replace(format.getWarnAfter(), values);
  }

  public String after(ProceedingJoinPoint joinPoint, Loggable loggable, Object result, long nano) {
    Map<String, Object> values = new HashMap<>();
    values.put(NAME_KEY, methodName(joinPoint));
    values.put(ARGS_KEY, methodArgs(joinPoint, loggable));
    values.put(DURATION_KEY, durationString(nano));
    values.put(RESULT_KEY, methodResults(result, loggable));
    return StrSubstitutor.replace(format.getAfter(), values);
  }

  public String error(ProceedingJoinPoint joinPoint, Loggable loggable, long nano, Throwable err) {
    Map<String, Object> values = new HashMap<>();
    values.put(NAME_KEY, methodName(joinPoint));
    values.put(ARGS_KEY, methodArgs(joinPoint, loggable));
    values.put(DURATION_KEY, durationString(nano));
    values.put(ERROR_CLASS_KEY, errClass(err));
    values.put(ERROR_MSG_KEY, errMsg(err));
    values.put(ERROR_SOURCE_CLASS_KEY, errSourceClass(err));
    values.put(ERROR_LINE_KEY, errLine(err));
    return StrSubstitutor.replace(format.getError(), values);
  }

  private String warnDuration(Loggable loggable) {
    return Duration.ofMillis(loggable.warnUnit().toMillis(loggable.warnOver())).toString();
  }

  private String methodName(JoinPoint joinPoint) {
    return ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
  }

  private String methodArgs(JoinPoint joinPoint, Loggable loggable) {
    return loggable.skipArgs() ? DOTS : argsToString(joinPoint.getArgs());
  }

  private String methodResults(Object result, Loggable loggable) {
    return loggable.skipResult() ? DOTS : argsToString(result);
  }

  private String errClass(Throwable err) {
    return err.getClass().getName();
  }

  private String errMsg(Throwable err) {
    return err.getMessage();
  }

  private int errLine(Throwable err) {
    if (err.getStackTrace().length > 0) {
      return err.getStackTrace()[0].getLineNumber();
    }
    return -1;
  }

  private String errSourceClass(Throwable err) {
    if (err.getStackTrace().length > 0) {
      return err.getStackTrace()[0].getClassName();
    }
    return "somewhere";
  }

  private String durationString(long nano) {
    return Duration.ofMillis(TimeUnit.NANOSECONDS.toMillis(nano)).toString();
  }

  private String argsToString(Object arg) {
    String text;
    if (arg == null) {
      return "NULL";
    } else if (arg.getClass().isArray()) {
      if (arg instanceof Object[]) {
        text = objectArraysToString((Object[]) arg);
      } else {
        text = primitiveArrayToString(arg);
      }
    } else {
      String origin = arg.toString();
      if (arg instanceof String || origin.isEmpty()) {
        text = String.format("'%s'", origin);
      } else {
        text = origin;
      }
    }
    return text;
  }

  private String objectArraysToString(Object... arg) {
    StringBuilder bldr = new StringBuilder();
    bldr.append('[');
    for (Object item : arg) {
      if (bldr.length() > 1) {
        bldr.append(",").append(" ");
      }
      bldr.append(argsToString(item));
    }
    return bldr.append(']').toString();
  }

  private String primitiveArrayToString(Object arg) {
    String text;
    if (arg instanceof char[]) {
      text = Arrays.toString((char[]) arg);
    } else if (arg instanceof byte[]) {
      text = Arrays.toString((byte[]) arg);
    } else if (arg instanceof short[]) {
      text = Arrays.toString((short[]) arg);
    } else if (arg instanceof int[]) {
      text = Arrays.toString((int[]) arg);
    } else if (arg instanceof long[]) {
      text = Arrays.toString((long[]) arg);
    } else if (arg instanceof float[]) {
      text = Arrays.toString((float[]) arg);
    } else if (arg instanceof double[]) {
      text = Arrays.toString((double[]) arg);
    } else if (arg instanceof boolean[]) {
      text = Arrays.toString((boolean[]) arg);
    } else {
      text = "[unknown]";
    }
    return text;
  }
}
