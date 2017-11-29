package com.pixel.okhttp.manage;

import android.support.annotation.NonNull;

import com.pixel.okhttp.callback.DownloadProgressCallback;
import com.pixel.okhttp.callback.ResultCallback;
import com.pixel.okhttp.callback.UploadProgressCallback;
import com.pixel.okhttp.interceptor.HeaderInterceptor;
import com.pixel.okhttp.interceptor.ParamInterceptor;
import com.pixel.okhttp.interceptor.ResponseInterceptor;
import com.pixel.okhttp.others.ConsEnum;
import com.pixel.okhttp.others.GsonUtil;
import com.pixel.okhttp.others.HttpException;
import com.pixel.okhttp.others.OkHttpOption;
import com.pixel.okhttp.others.OkHttpUploadBody;
import com.pixel.okhttp.others.ThreadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.pixel.okhttp.others.ThreadUtil.runOnUi;

/**
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class OkHttpManage implements HttpClient {
    private static OkHttpManage mHttpControl;
    private static OkHttpOption okHttpOption;

    /**
     * 设置OkHttp客户端参数 一定要在所有网络请求调用之前设置
     */
    public static void setOkHttpOption(OkHttpOption okHttpOption) {
        OkHttpManage.okHttpOption = okHttpOption;
    }

    public static OkHttpManage getHttpClient() {
        return getHttpClient(null);
    }

    public synchronized static OkHttpManage getHttpClient(OkHttpOption okHttpOption) {
        if (mHttpControl == null || okHttpOption != null) {
            OkHttpManage.setOkHttpOption(okHttpOption);
            mHttpControl = new OkHttpManage();
        }
        return mHttpControl;
    }

    private OkHttpClient mOkHttpClient;

    private OkHttpManage() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        // 获取配置参数
        if (okHttpOption != null) {
            if (okHttpOption.getCookieJar() != null) {
                builder.cookieJar(okHttpOption.getCookieJar());
            }
            if (okHttpOption.getPubHeader() != null) {
                builder.addInterceptor(new HeaderInterceptor(okHttpOption.getPubHeader()));
            }
            if (okHttpOption.getPubParam() != null) {
                builder.addInterceptor(new ParamInterceptor(okHttpOption.getPubParam()));
            }
            if (okHttpOption.getCacheDir() != null) {
                builder.cache(new Cache(okHttpOption.getCacheDir(), 10 * 1024 * 1024));
            }
        }

        mOkHttpClient = builder.build();
    }

    private void executionRequest(final Call call, final ResultCallback callback) {
        callback.start();
        ThreadUtil.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = call.execute();   // 发起请求 需要在子线程执行
                    if (response.isSuccessful()) {
                        String jsonString = response.body().string();
                        Type jsonType = GsonUtil.getGsonType(callback.getClass());
                        Object object = null;
                        if (jsonType == String.class) {
                            object = jsonString;
                        } else {
                            object = GsonUtil.getGson().fromJson(jsonString, jsonType);
                        }
                        final Object finalObject = object;
                        runOnUi(new Runnable() {
                            @Override
                            public void run() {
                                callback.succeed(finalObject);
                            }
                        });
                    } else {
                        runOnUi(new Runnable() {
                            @Override
                            public void run() {
                                callback.error(new HttpException(response.toString(), ConsEnum.服务器响应code不为200.toString()));
                            }
                        });
                    }
                    response.body().close();
                } catch (Exception e) {
                    callback.error(new HttpException(e.getMessage(), ConsEnum.发送请求异常或者JSON解析异常.toString()));
                }
            }
        });
    }

    @Override
    public void get(@NonNull String url, @NonNull ResultCallback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        executionRequest(mOkHttpClient.newCall(request), callback);
    }

    @Override
    public void post(@NonNull String url, @NonNull Map<String, String> parameter, @NonNull ResultCallback callback) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            bodyBuilder.add(entry.getKey(), entry.getValue());
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(bodyBuilder.build());
        Request request = builder.build();
        executionRequest(mOkHttpClient.newCall(request), callback);
    }

    @Override
    public void json(String url, String json, ResultCallback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json));
        Request request = builder.build();
        executionRequest(mOkHttpClient.newCall(request), callback);
    }

    @Override
    public void upload(@NonNull String url, @NonNull File file, @NonNull ResultCallback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file));
        Request request = builder.build();
        executionRequest(mOkHttpClient.newCall(request), callback);
    }

    public void upload(@NonNull String url, Map<String, String> parameter, String[] names, @NonNull File[] files, @NonNull ResultCallback callback) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        if (names.length != files.length) {
            callback.error(new HttpException(ConsEnum.文件名要与文件对应.toString()));
            return;
        }
        for (int i = 0; i < names.length; i++) {
            bodyBuilder.addFormDataPart(names[i], names[i], RequestBody.create(MediaType.parse("application/octet-stream"), files[i]));
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(bodyBuilder.build());
        Request request = builder.build();
        executionRequest(mOkHttpClient.newCall(request), callback);
    }

    public void uploadOkHttp(
            @NonNull String url, @NonNull Map<String, String> parameter, @NonNull String[] fileNames, @NonNull File[] fileValues, @NonNull final ResultCallback callback) {
        OkHttpUploadBody.Builder body = new OkHttpUploadBody.Builder();
        body.setType(MultipartBody.FORM);
        if (parameter != null) {
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                body.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                        RequestBody.create(null, entry.getValue()));
            }
        }
        if (fileValues.length > 0 && fileNames.length > 0 && fileNames.length == fileValues.length) {
            for (int i = 0; i < fileValues.length; i++) {
                String fileName = fileValues[i].getName();
                String mime = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                RequestBody fileBody = RequestBody.create(
                        MediaType.parse(mime != null ? mime : "application/x-zip-compressed"), fileValues[i]);
                body.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + fileNames[i] + "\"; filename=\"" + fileName + "\""), fileBody);
            }
        } else {
            callback.error(new HttpException(ConsEnum.文件名要与文件对应.toString()));
            return;
        }
        OkHttpUploadBody uploadEntity = body.build();
        uploadEntity.setUploadProgressInterface(new UploadProgressCallback() {
            @Override
            public void doProgress(final Long progressLength, final Long contentLength, final Boolean complete) {
                if (callback != null) {
                    ThreadUtil.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            callback.progress(progressLength, contentLength, complete);
                        }
                    });
                }
            }
        });
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(uploadEntity);
        Request request = builder.build();
        executionRequest(mOkHttpClient.newCall(request), callback);
    }

    @Override
    public void download(@NonNull String url, @NonNull final String path, @NonNull final ResultCallback callback) {
        final long startDownTime = System.currentTimeMillis();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        callback.start();
        mOkHttpClient.newBuilder().addInterceptor(new ResponseInterceptor(new DownloadProgressCallback() {
            @Override
            public void onDownProgress(final Long progress, final Long contentLength, final Boolean complete) {
                ThreadUtil.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        callback.progress(progress, contentLength, complete);
                        if (complete) {
                            callback.succeed(System.currentTimeMillis() - startDownTime);
                        }
                    }
                });
            }
        })).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                ThreadUtil.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        callback.error(new HttpException(e.getMessage(), ConsEnum.下载失败.toString()));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = null;
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(path);
                        inputStream = response.body().byteStream();
                        byte[] buffer = new byte[1024];
                        int hasRead = 0;
                        while ((hasRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, hasRead);
                        }
                        outputStream.flush();
                    } catch (Exception e) {
                        callback.error(new HttpException(e.getMessage(), ConsEnum.下载失败.toString()));
                    } finally {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    response.body().close();
                } else {
                    ThreadUtil.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            callback.error(new HttpException(ConsEnum.服务器响应code不为200.toString(), ConsEnum.下载失败.toString()));
                        }
                    });
                }
            }
        });
    }

    // 获取网络文件大小
    public void getContentLength(String url, Callback callback) {
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        //创建请求会话
        Call call = mOkHttpClient.newCall(request);
        //同步执行会话请求
        call.enqueue(callback);
    }

    // 下载网络文件指定区域
    public void downloadFileByRange(String url, long startIndex, long endIndex, Callback callback) {
        // 创建一个Request 设置分段下载的头信息。 Range:做分段数据请求,断点续传指示下载的区间。格式: Range bytes=0-1024或者bytes:0-1024
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(url)
                .build();
        //创建请求会话
        Call call = mOkHttpClient.newCall(request);
        //同步执行会话请求
        call.enqueue(callback);
    }
}
