package lazycode.concurrent;

import java.util.concurrent.*;

public class LzThreadPool {
    ThreadPoolExecutor threadPool = null;

    public LzThreadPool() {
        this.init(10);
    }

    public LzThreadPool(int threadNum) {
        this.init(threadNum);
    }

    public void init(int threadNum) {
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    }

    public <V> FutureTask<V> addTask(Callable<V> callable) {
        FutureTask<V> futureTask = new FutureTask<V>(callable);
        this.threadPool.submit(futureTask);
        return futureTask;
    }

    public <V> Object getTaskResult(FutureTask<V> futureTask) throws ExecutionException, InterruptedException {
        return futureTask.get();
    }

    public void close() {
        if (this.threadPool != null) {
            this.threadPool.shutdown();
        }
    }

}
