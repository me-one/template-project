package com.megvii.insight.framework.logger;

import com.megvii.insight.framework.logger.annotation.Loggable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Helper class for log message format.
 */
final class LoggerFormatter {

  private static final String DOTS = "...";

  private static final String METHOD_NAME = "method.name";
  private static final String METHOD_ARGS = "method.args";
  private static final String METHOD_RESULT = "method.result";
  private static final String METHOD_DURATION = "method.duration";
  private static final String METHOD_WARN_DURATION = "method.warn.duration";
  private static final String ERROR_CLASS_NAME = "error.class.name";
  private static final String ERROR_MESSAGE = "error.message";
  private static final String ERROR_SOURCE_CLASS_NAME = "error.source.class.name";
  private static final String ERROR_SOURCE_LINE = "error.source.line";
  private static final String ERROR_STACKTRACE = "error.stacktrace";

  private LoggerFormat format;

  public LoggerFormatter(LoggerFormat format) {
    this.format = format;
  }

  public String enter(ProceedingJoinPoint joinPoint, Loggable loggable) {
    Map<String, Object> values = new HashMap<>();
    values.put(METHOD_NAME, getMethodName(joinPoint));
    values.put(METHOD_ARGS, getMethodArgs(joinPoint, loggable));
    return StrSubstitutor.replace(format.getEnter(), values);
  }

  public String warnBefore(ProceedingJoinPoint joinPoint, Loggable loggable, long nano) {
    Map<String, Object> values = new HashMap<>();
    values.put(METHOD_NAME, getMethodName(joinPoint));
    values.put(METHOD_ARGS, getMethodArgs(joinPoint, loggable));
    values.put(METHOD_DURATION, durationToString(nano));
    values.put(METHOD_WARN_DURATION, getMethodWarnDuration(loggable));
    return StrSubstitutor.replace(format.getWarnBefore(), values);
  }

  public String warnAfter(ProceedingJoinPoint joinPoint, Loggable loggable, Object result, long nano) {
    Map<String, Object> values = new HashMap<>();
    values.put(METHOD_NAME, getMethodName(joinPoint));
    values.put(METHOD_ARGS, getMethodArgs(joinPoint, loggable));
    values.put(METHOD_DURATION, durationToString(nano));
    values.put(METHOD_WARN_DURATION, getMethodWarnDuration(loggable));
    values.put(METHOD_RESULT, getMethodResults(result, loggable));
    return StrSubstitutor.replace(format.getWarnAfter(), values);
  }

  public String after(ProceedingJoinPoint joinPoint, Loggable loggable, Object result, long nano) {
    Map<String, Object> values = new HashMap<>();
    values.put(METHOD_NAME, getMethodName(joinPoint));
    values.put(METHOD_ARGS, getMethodArgs(joinPoint, loggable));
    values.put(METHOD_DURATION, durationToString(nano));
    values.put(METHOD_RESULT, getMethodResults(result, loggable));
    return StrSubstitutor.replace(format.getAfter(), values);
  }

  public String error(ProceedingJoinPoint joinPoint, Loggable loggable, long nano, Throwable err) {
    Map<String, Object> values = new HashMap<>();
    values.put(METHOD_NAME, getMethodName(joinPoint));
    values.put(METHOD_ARGS, getMethodArgs(joinPoint, loggable));
    values.put(METHOD_DURATION, durationToString(nano));
    values.put(ERROR_CLASS_NAME, getErrorClass(err));
    values.put(ERROR_MESSAGE, getErrorMsg(err));
    values.put(ERROR_SOURCE_CLASS_NAME, getErrorSourceClass(err));
    values.put(ERROR_SOURCE_LINE, getErrorLine(err));
    values.put(ERROR_STACKTRACE, getErrorStackTrace(err, loggable));
    return StrSubstitutor.replace(format.getError(), values);
  }

  private String getMethodWarnDuration(Loggable loggable) {
    return Duration.ofMillis(loggable.warnUnit().toMillis(loggable.warnOver())).toString();
  }

  private String getMethodName(JoinPoint joinPoint) {
    return ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
  }

  private String getMethodArgs(JoinPoint joinPoint, Loggable loggable) {
    return loggable.skipArgs() ? DOTS : argsToString(joinPoint.getArgs());
  }

  private String getMethodResults(Object result, Loggable loggable) {
    return loggable.skipResult() ? DOTS : argsToString(result);
  }

  private String getErrorClass(Throwable err) {
    return err.getClass().getName();
  }

  private String getErrorMsg(Throwable err) {
    return err.getMessage();
  }

  private int getErrorLine(Throwable err) {
    if (err.getStackTrace().length > 0) {
      return err.getStackTrace()[0].getLineNumber();
    }
    return -1;
  }

  private String getErrorStackTrace(Throwable err, Loggable loggable) {
    if (err.getStackTrace().length > 0) {
      return loggable.skipStackTrace() ? DOTS : ExceptionUtils.getStackTrace(err);
    }
    return "";
  }

  private String getErrorSourceClass(Throwable err) {
    if (err.getStackTrace().length > 0) {
      return err.getStackTrace()[0].getClassName();
    }
    return "somewhere";
  }

  private String durationToString(long nano) {
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
