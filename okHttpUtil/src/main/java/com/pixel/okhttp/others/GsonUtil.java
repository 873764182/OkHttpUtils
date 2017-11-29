package com.pixel.okhttp.others;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Administrator
 * @date 2017/11/28 0028
 */

public class GsonUtil {

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 获取对象JSON类型
     */
    public static Type getGsonType(@NonNull Class<?> cls) {
        Type superclass = cls.getGenericSuperclass();
        if (superclass == String.class) {
            return new TypeToken<String>() {
            }.getType();
        } else {
            return $Gson$Types.canonicalize(((ParameterizedType) superclass).getActualTypeArguments()[0]);
        }
    }

    /**
     * 获取GSON对象
     */
    public static Gson getGson() {
        return gson;
    }

}
