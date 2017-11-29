package com.pixel.okhttp.callback;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public interface UploadProgressCallback {
    void doProgress(Long progress, Long contentLength, Boolean complete);
}
