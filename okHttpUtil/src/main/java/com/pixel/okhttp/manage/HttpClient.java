package com.pixel.okhttp.manage;

import com.pixel.okhttp.callback.ResultCallback;

import java.io.File;
import java.util.Map;

/**
 * 约定
 *
 * @author Administrator
 * @date 2017/11/28 0028
 */

public interface HttpClient {

    // get请求
    void get(String url, ResultCallback callback);

    // post请求
    void post(String url, Map<String, String> parameter, ResultCallback callback);

    // 提交JSON
    void json(String url, String json, ResultCallback callback);

    // 上传文件
    void upload(String url, File file, ResultCallback callback);

    // 下载文件
    void download(String url, String path, ResultCallback callback);

}
