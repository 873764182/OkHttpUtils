package com.pixel.okhttp.interceptor;

import com.pixel.okhttp.callback.DownloadProgressCallback;
import com.pixel.okhttp.others.OkHttpDownloadBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Administrator
 * @date 2017/11/29 0029
 * <p>
 * OkHttp响应拦截 回传下载进度
 */

public class ResponseInterceptor implements Interceptor {
    private DownloadProgressCallback downloadProgressCallback;

    public ResponseInterceptor(DownloadProgressCallback downloadProgressCallback) {
        this.downloadProgressCallback = downloadProgressCallback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //拦截
        Response originalResponse = chain.proceed(chain.request());
        //包装响应体并返回
        return originalResponse.newBuilder().body(
                new OkHttpDownloadBody(originalResponse.body(), downloadProgressCallback)).build();
    }
}
