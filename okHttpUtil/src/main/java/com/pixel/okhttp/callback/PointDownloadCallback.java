package com.pixel.okhttp.callback;

/**
 * 断点下载事件回调
 *
 * @author Administrator
 * @date 2017/11/29 0029
 */

public interface PointDownloadCallback {
    void onFinished();

    void onProgress(float progress);

    void onPause();

    void onCancel();
}
