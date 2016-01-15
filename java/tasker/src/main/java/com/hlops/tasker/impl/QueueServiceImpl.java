package com.hlops.tasker.impl;

import com.hlops.tasker.QueueService;
import com.hlops.tasker.task.CacheableTask;
import com.hlops.tasker.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: a.karnachuk
 * Date: 12/28/13
 * Time: 3:40 PM
 */
@Service
public class QueueServiceImpl implements QueueService {

    private ThreadPoolExecutor threadExecutor;
    private final ConcurrentHashMap<Object, Future> cache = new ConcurrentHashMap<Object, Future>();
    private final AtomicInteger poolSize = new AtomicInteger();

    public QueueServiceImpl() {
    }

    @Override
    public void destroy() throws Exception {
        threadExecutor.shutdown();
    }

    @PostConstruct
    private void init() {

        int poolSize = 10;
        this.poolSize.set(poolSize);

        threadExecutor = new ThreadPoolExecutor(poolSize, poolSize, 60L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(), new QueueServiceThreadFactory()) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                final Task task = ((PriorityFutureTask) r).getTask();
                task.beforeExecute(t);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                final Task task = ((PriorityFutureTask) r).getTask();
                task.afterExecute(t);
                checkPoolSize(false);
            }

            @Override
            protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) throws TaskAlreadyInCacheException {
                PriorityFutureTask<T> result = null;
                Task<T> task = (Task<T>) callable;
                Object id = getId(task);
                if (id != null) {
                    //noinspection unchecked
                    RunnableFuture<T> cached = (RunnableFuture<T>) cache.get(id);
                    if (cached != null) {
                        if (cached instanceof PriorityFutureTask && ((PriorityFutureTask) cached).expired()) {
                            cache.remove(id, cached);
                        } else {
                            return cached;
                        }
                    }
                    result = new PriorityFutureTask<T>((Task<T>) callable);
                    //noinspection unchecked
                    cached = (RunnableFuture<T>) cache.putIfAbsent(id, result);
                    if (cached != null) {
                        throw new TaskAlreadyInCacheException(cached);
                    }
                }

                checkPoolSize(true);
                if (result == null) {
                    result = new PriorityFutureTask<T>((Task<T>) callable);
                }
                return result;
            }
        };
    }

    public <T> Future<T> executeTask(Task<T> task) {
        //noinspection unchecked
        try {
            return threadExecutor.submit(task);
        } catch (TaskAlreadyInCacheException e) {
            //noinspection unchecked
            return e.getCachedTask();
        }
    }

    private void checkPoolSize(boolean isIncrement) {
        if (Thread.currentThread() instanceof QueueServiceThread) {
            if (isIncrement) {
                if (((QueueServiceThread) Thread.currentThread()).setPoolSizeInflated(true)) {
                    updatePoolSize(poolSize.incrementAndGet());
                }
            } else {
                if (((QueueServiceThread) Thread.currentThread()).setPoolSizeInflated(false)) {
                    updatePoolSize(poolSize.decrementAndGet());
                }
            }
        }
    }

    private synchronized void updatePoolSize(int n) {
        threadExecutor.setCorePoolSize(n);
        threadExecutor.setMaximumPoolSize(n);
    }

    private <T> Object getId(Task<T> task) {
        if (task instanceof CacheableTask<?>) {
            return ((CacheableTask) task).getId();
        }
        return null;
    }

}
