package com.megvii.insight.common.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.beans.Introspector;

/**
 * Spring Bean Factory
 */
public class SpringBeanFactory implements ApplicationContextAware {

  private static SpringBeanFactory instance = null;

  private static ApplicationContext applicationContext;

  public static SpringBeanFactory getInstance() {
    if (instance == null) {
      instance = new SpringBeanFactory();
    }
    return instance;
  }

  public static Object getBean(String name) throws BeansException {
    return applicationContext.getBean(name);
  }

  public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    return applicationContext.getBean(name, requiredType);
  }

  public static <T> T getBean(Class<T> requiredType) throws BeansException {
    return applicationContext.getBean(Introspector.decapitalize(requiredType.getSimpleName()), requiredType);
  }

  public static Object getBean(String name, Object args) throws BeansException {
    return applicationContext.getBean(name, args);
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(
      @SuppressWarnings("NullableProblems") ApplicationContext applicationContext) throws BeansException {
    SpringBeanFactory.applicationContext = applicationContext;
  }

}