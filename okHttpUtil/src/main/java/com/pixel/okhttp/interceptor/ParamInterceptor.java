package com.pixel.okhttp.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 公共参数添加拦截器
 *
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class ParamInterceptor implements Interceptor {
    private Map<String, String> mParamMap;

    public ParamInterceptor(@NonNull Map<String, String> paramMap) {
        this.mParamMap = paramMap;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if ("GET".equalsIgnoreCase(request.method())) {
            HttpUrl.Builder builder = request.url().newBuilder();
            for (Map.Entry<String, String> entry : mParamMap.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            // 带上一个时间戳让请求不缓存
            builder.addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()));
            HttpUrl httpUrl = builder.build();
            request = request.newBuilder().url(httpUrl).build();
        } else if ("POST".equalsIgnoreCase(request.method())) {
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
                for (Map.Entry<String, String> entry : mParamMap.entrySet()) {
                    bodyBuilder.addEncoded(entry.getKey(), entry.getValue());
                }
                // 带上一个时间戳让请求不缓存
                bodyBuilder.addEncoded("timestamp", String.valueOf(System.currentTimeMillis()));
                formBody = bodyBuilder.build();
                request = request.newBuilder().post(formBody).build();
            } else {
                Log.e(getClass().getSimpleName(), "未能添加公共参数 instanceof");
            }
        } else {
            Log.e(getClass().getSimpleName(), "未能添加公共参数 method");
        }
        return chain.proceed(request);
    }
}
