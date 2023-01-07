package lazycode.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class LzThread<V> {
    FutureTask<V> futureTask = null;
    Thread thread = null;


    public LzThread(Callable<V> callable) {
        this.init(callable);
    }

    public LzThread<V> init(Callable<V> callable) {
        this.futureTask = new FutureTask<V>(callable);
        this.thread = new Thread(this.futureTask);
        return this;
    }

    public LzThread<V> startRunTask() {
        if (this.thread != null) {
            this.thread.start();
        } else {
            this.thread = new Thread(this.futureTask);
        }
        return this;
    }

    public V getResult() throws ExecutionException, InterruptedException {
        if (!this.thread.isAlive()) {
            this.startRunTask();
        }
        return this.futureTask.get();
    }

}
