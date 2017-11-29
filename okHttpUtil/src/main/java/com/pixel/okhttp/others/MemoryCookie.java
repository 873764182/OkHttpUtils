package com.pixel.okhttp.others;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 内存级别的cookie保存
 *
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class MemoryCookie implements CookieJar {
    private final HashMap<String, List<Cookie>> cookieMap = new HashMap<>();    // cookie没有保存到持久化

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieMap.put(url.host(), cookies); // 保存服务器响应cookie
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieMap.get(url.host());   // 带上cookie到服务器
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}
