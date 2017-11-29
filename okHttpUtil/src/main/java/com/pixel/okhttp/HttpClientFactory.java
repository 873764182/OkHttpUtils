package com.pixel.okhttp;

import com.pixel.okhttp.manage.HttpClient;
import com.pixel.okhttp.manage.OkHttpManage;
import com.pixel.okhttp.others.ClientEnum;

/**
 * @author Administrator
 * @date 2017/11/29 0029
 */

public abstract class HttpClientFactory {

    public static HttpClient getClient(ClientEnum client) {
        if (client == ClientEnum.OK_HTTP) {
            return OkHttpManage.getHttpClient();
        } else {
            return OkHttpManage.getHttpClient();
        }
    }

}
