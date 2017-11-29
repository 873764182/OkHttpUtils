package com.pixel.okhttp.others;

import com.pixel.okhttp.callback.DownloadProgressCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 重写OkHttp下载对象
 *
 * @author Administrator
 * @date 2017/11/29 0029
 */

public class OkHttpDownloadBody extends ResponseBody {
    private volatile ResponseBody responseBody;
    private volatile BufferedSource bufferedSource;
    private volatile DownloadProgressCallback downloadProgressCallback;

    public OkHttpDownloadBody(ResponseBody responseBody, DownloadProgressCallback downloadProgressCallback) {
        this.responseBody = responseBody;
        this.downloadProgressCallback = downloadProgressCallback;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));//包装
        }
        return bufferedSource;
    }

    /* 读取 回调进度接口 */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            long contentLength = responseBody.contentLength();  // 总大小 如果contentLength()不知道长度，会返回-1
            long totalBytesRead = 0L;   //当前累计读取字节数

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                // 返回一次读取的数据量
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += (bytesRead != -1 ? bytesRead : 0);
                //回调
                if (downloadProgressCallback != null) {
                    downloadProgressCallback.onDownProgress(totalBytesRead, contentLength, bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }

}
