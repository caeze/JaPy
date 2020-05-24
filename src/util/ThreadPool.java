package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Class for a thread pool that reuses threads.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ThreadPool {

    private static final int THREAD_POOL_SIZE = 3;

    private static ThreadPool instance;
    private ThreadPoolExecutor threadPoolExecutor;

    private ThreadPool() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static ThreadPool getInstance() {
        if (instance == null) {
            instance = new ThreadPool();
            instance.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        }
        return instance;
    }

    public void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
        threadPoolExecutor.shutdownNow();
        instance = null;
    }
}
