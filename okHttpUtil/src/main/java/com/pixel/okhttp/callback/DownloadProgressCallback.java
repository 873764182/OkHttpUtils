package com.pixel.okhttp.callback;

/**
 *
 * @author Administrator
 * @date 2017/11/29 0029
 */

public interface DownloadProgressCallback {
    void onDownProgress(Long progressLength, Long contentLength, Boolean complete);
}
