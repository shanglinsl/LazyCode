package temp;

import lazycode.*;
import lazycode.concurrent.LzThread;
import lazycode.concurrent.LzThreadPool;
import org.apache.commons.lang3.ArrayUtils;
import sun.nio.ch.ThreadPool;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class TestMain {
    public static void main(String[] args) throws Exception {

        String[] strings = new String[]{"a", "b", "c", "d", "e"};

        System.out.println(LzArrayTool.toString(LzArrayTool.addAll(strings, "f", "g", "h")));


        LzConcurrent.thread(new Callable<String>() {
            @Override
            public String call() throws Exception {
                for (int i = 0; i < 3; i++) {
                    System.out.println("thread1 " + i);
                    Thread.sleep(1000);
                }
                return "success1";
            }
        }).getResult();

        LzConcurrent.thread(new Callable<String>() {
            @Override
            public String call() throws Exception {
                for (int i = 0; i < 3; i++) {
                    System.out.println("thread2 " + i);
                    Thread.sleep(1000);
                }
                return "success2";
            }
        }).getResult();
    }
}


