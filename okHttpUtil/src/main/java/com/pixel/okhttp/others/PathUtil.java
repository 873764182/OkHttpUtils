package com.pixel.okhttp.others;

import android.content.Context;

import java.io.File;

/**
 * 获取Android资源路径
 *
 * @author Administrator
 * @date 2017/11/29 0029
 */

public class PathUtil {

    // 获取系统文件路径分隔符
    public static char getSeparatorChar() {
        return File.separatorChar;
    }

    // 获取应用文件目录
    public static String getAppFilePath(Context context) {
        return context.getFilesDir().getAbsolutePath() + getSeparatorChar();
    }

    // 获取应用缓存目录
    public static String getAppCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + getSeparatorChar();
    }

    // 获取应用外部缓存目录
    public static String getAppExternalCachePath(Context context) {
        return context.getExternalCacheDir().getAbsolutePath() + getSeparatorChar();
    }

    // Opaque Binary Blob 在SD卡上应用的私有目录
    public static String getObbPath(Context context) {
        return context.getObbDir().getAbsolutePath() + getSeparatorChar();
    }

    /**
     * {@link android.os.Environment#DIRECTORY_MUSIC},
     * {@link android.os.Environment#DIRECTORY_PODCASTS},
     * {@link android.os.Environment#DIRECTORY_RINGTONES},
     * {@link android.os.Environment#DIRECTORY_ALARMS},
     * {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     * {@link android.os.Environment#DIRECTORY_PICTURES}, or
     * {@link android.os.Environment#DIRECTORY_MOVIES}.
     */
    public static String getAppExternalFilePath(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath() + getSeparatorChar();
    }


}
