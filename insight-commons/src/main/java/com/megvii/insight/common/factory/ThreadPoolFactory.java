package com.megvii.insight.common.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread Pool Factory
 *
 *
 * <pre>
 *
 *    e.g.
 *
 *    ThreadPoolFactory.createFixedThreadPool("poolName").getExecutor()
 *
 * </pre>
 */
public class ThreadPoolFactory {

  private static ConcurrentHashMap<String, ThreadPoolFactory> threadPoolFactories;

  private ExecutorService executor;

  private ThreadPoolFactory(final String poolName) {
    this.executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors(),
        new ThreadFactory() {
          private final AtomicInteger ai = new AtomicInteger(0);

          @Override
          public Thread newThread(@SuppressWarnings("NullableProblems") Runnable runnable) {
            return new Thread(runnable, poolName + "_thread_" + ai.incrementAndGet());
          }
        });
  }

  private ThreadPoolFactory(final String poolName, int nThreads) {
    this.executor = Executors.newFixedThreadPool(nThreads,
        new ThreadFactory() {
          private final AtomicInteger ai = new AtomicInteger(0);

          @Override
          public Thread newThread(@SuppressWarnings("NullableProblems") Runnable runnable) {
            return new Thread(runnable, poolName + "_thread_" + ai.incrementAndGet());
          }
        });
  }

  /**
   * 获取线程池，默认并发线程数 Runtime.getRuntime().availableProcessors()
   *
   * @param poolName 线程池名称
   * @return {@link ThreadPoolFactory}
   */
  public static ThreadPoolFactory createFixedThreadPool(String poolName) {
    return Get.fixedThreadPool(poolName);
  }

  /**
   * @param poolName 线程池名称
   * @param nThreads 并发线程数
   * @return {@link ThreadPoolFactory}
   */
  public static ThreadPoolFactory createFixedThreadPool(String poolName, int nThreads) {
    return Get.fixedThreadPool(poolName, nThreads);
  }

  /**
   *
   */
  public ExecutorService getExecutor() {
    return this.executor;
  }

  private static class Get {
    //
    static ThreadPoolFactory fixedThreadPool(String poolName) {
      if (ThreadPoolFactory.threadPoolFactories == null) {
        ThreadPoolFactory.threadPoolFactories = new ConcurrentHashMap<>();
      }
      if (!ThreadPoolFactory.threadPoolFactories.containsKey(poolName)) {
        ThreadPoolFactory.threadPoolFactories.put(poolName, new ThreadPoolFactory(poolName));
      }
      return ThreadPoolFactory.threadPoolFactories.get(poolName);
    }

    //
    static ThreadPoolFactory fixedThreadPool(String poolName, int nThreads) {
      if (ThreadPoolFactory.threadPoolFactories == null) {
        ThreadPoolFactory.threadPoolFactories = new ConcurrentHashMap<>();
      }
      if (!ThreadPoolFactory.threadPoolFactories.containsKey(poolName)) {
        ThreadPoolFactory.threadPoolFactories.put(poolName, new ThreadPoolFactory(poolName, nThreads));
      }
      return ThreadPoolFactory.threadPoolFactories.get(poolName);
    }
  }
}


