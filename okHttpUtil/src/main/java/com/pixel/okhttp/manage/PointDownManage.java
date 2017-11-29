package com.pixel.okhttp.manage;

import android.os.Environment;
import android.text.TextUtils;

import com.pixel.okhttp.callback.PointDownloadCallback;
import com.pixel.okhttp.others.FilePoint;
import com.pixel.okhttp.task.PointDownTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 断点下载任务管理器
 * <p>
 *
 * @author https://github.com/ausboyue/Okhttp-Multiple-Thread-Download-Demo
 * @date 2017/11/29 0029
 */

public class PointDownManage {
    // 单例对象
    private static PointDownManage mInstance;
    //默认下载目录
    private String DEFAULT_FILE_DIR;
    //文件下载任务索引，String为url,用来唯一区别并操作下载的文件
    private Map<String, PointDownTask> mDownloadTasks;

    /**
     * 下载文件
     */
    public void download(String... urls) {
        //单任务开启下载或多任务开启下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                mDownloadTasks.get(url).start();
            }
        }
    }

    // 获取下载文件的名称
    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 暂停
     */
    public void pause(String... urls) {
        //单任务暂停或多任务暂停下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                mDownloadTasks.get(url).pause();
            }
        }
    }

    /**
     * 取消下载
     */
    public void cancel(String... urls) {
        //单任务取消或多任务取消下载
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                mDownloadTasks.get(url).cancel();
            }
        }
    }

    /**
     * 添加下载任务
     */
    public void add(String url, PointDownloadCallback l) {
        add(url, null, null, l);
    }

    /**
     * 添加下载任务
     */
    public void add(String url, String filePath, PointDownloadCallback l) {
        add(url, filePath, null, l);
    }

    /**
     * 添加下载任务
     */
    public void add(String url, String filePath, String fileName, PointDownloadCallback l) {
        if (TextUtils.isEmpty(filePath)) {//没有指定下载目录,使用默认目录
            filePath = getDefaultDirectory();
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = getFileName(url);
        }
        mDownloadTasks.put(url, new PointDownTask(new FilePoint(url, filePath, fileName), l));
    }

    /**
     * 默认下载目录
     *
     * @return
     */
    private String getDefaultDirectory() {
        if (TextUtils.isEmpty(DEFAULT_FILE_DIR)) {
            DEFAULT_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + getClass().getSimpleName() + File.separator;
        }
        return DEFAULT_FILE_DIR;
    }

    public static PointDownManage getInstance() {//管理器初始化
        if (mInstance == null) {
            synchronized (PointDownManage.class) {
                if (mInstance == null) {
                    mInstance = new PointDownManage();
                }
            }
        }
        return mInstance;
    }

    public PointDownManage() {
        mDownloadTasks = new HashMap<>();
    }

    /**
     * 取消下载
     */
    public boolean isDownloading(String... urls) {
        //这里传一个url就是判断一个下载任务
        //多个url数组适合下载管理器判断是否作操作全部下载或全部取消下载
        boolean result = false;
        for (int i = 0, length = urls.length; i < length; i++) {
            String url = urls[i];
            if (mDownloadTasks.containsKey(url)) {
                result = mDownloadTasks.get(url).isDownloading();
            }
        }
        return result;
    }
}
