package com.pixel.okhttp.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头设置拦截器
 *
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class HeaderInterceptor implements Interceptor {
    private Map<String, String> mHeaderMap;

    public HeaderInterceptor(@NonNull Map<String, String> headerMap) {
        this.mHeaderMap = headerMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        for (Map.Entry<String, String> entry : mHeaderMap.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        builder.method(original.method(), original.body());
        Request request = builder.build();
        return chain.proceed(request);
    }
}
