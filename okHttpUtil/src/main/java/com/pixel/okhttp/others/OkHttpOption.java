package com.pixel.okhttp.others;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.Map;

import okhttp3.CookieJar;

/**
 * @author Administrator
 * @date 2017/11/28 0028
 * <p>
 * OkHttp客户端配置
 */

public class OkHttpOption {

    private Context context;
    private CookieJar cookieJar;
    private Map<String, String> pubHeader;
    private Map<String, String> pubParam;
    private File cacheDir;

    public OkHttpOption(Context context) {
        this.context = context;
    }

    public void openCache() {
        cacheDir = context.getCacheDir().getAbsoluteFile();
    }

    // 打开cookie
    public void openCookie() {
        this.cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    // 设置公共请求头
    public void setPublicHeader(Map<String, String> pubHeader) {
        this.pubHeader = pubHeader;
    }

    // 设置公共参数
    public void setPublicParam(Map<String, String> pubParam) {
        this.pubParam = pubParam;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Map<String, String> getPubHeader() {
        return pubHeader;
    }

    public Map<String, String> getPubParam() {
        return pubParam;
    }

    public File getCacheDir() {
        return cacheDir;
    }
}
