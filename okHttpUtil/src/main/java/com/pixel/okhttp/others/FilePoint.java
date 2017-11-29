package com.pixel.okhttp.others;

/**
 * 断点下载的文件
 *
 * @author Administrator
 * @date 2017/11/29 0029
 */

public class FilePoint {
    //文件名
    private String fileName;
    //下载地址
    private String url;
    //下载目录
    private String filePath;

    public FilePoint(String url) {
        this.url = url;
    }

    public FilePoint(String filePath, String url) {
        this.filePath = filePath;
        this.url = url;
    }

    public FilePoint(String url, String filePath, String fileName) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FilePoint{" +
                "fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
