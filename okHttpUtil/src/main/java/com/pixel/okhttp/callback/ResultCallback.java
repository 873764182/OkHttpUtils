package com.pixel.okhttp.callback;

import android.util.Log;

import com.pixel.okhttp.others.HttpException;

/**
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class ResultCallback<T extends Object> {

    // 开始
    public void start() {
        Log.e(getClass().getSimpleName(), "start");
    }

    // 成功
    public void succeed(T result) {
        complete();
    }

    // 错误
    public void error(HttpException exception) {
        exception.printStackTrace();
        complete();
    }

    // 进度
    public void progress(long progressLength, long contentLength, Boolean complete) {
        Log.e(getClass().getSimpleName(), "progress: " + progressLength);
    }

    // 无论如何都会执行
    public void complete() {
        Log.e(getClass().getSimpleName(), "onFinally");
    }

}
