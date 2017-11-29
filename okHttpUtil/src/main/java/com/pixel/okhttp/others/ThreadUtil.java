package com.pixel.okhttp.others;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class ThreadUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5, 20, 300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void runOnUi(Runnable runnable) {
        handler.post(runnable);
    }

    public static void runOnUi(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    public static void runAsync(Runnable runnable) {
        executor.execute(runnable);
    }

}
