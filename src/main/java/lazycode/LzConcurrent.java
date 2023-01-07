package lazycode;

import lazycode.concurrent.LzThread;
import lazycode.concurrent.LzThreadPool;

import java.util.concurrent.*;

public class LzConcurrent {

    public static LzThreadPool threadPool() {
        return new LzThreadPool();
    }

    public static <V> LzThread<V> thread(Callable<V> callable) {
        return new LzThread<V>(callable);
    }
}
