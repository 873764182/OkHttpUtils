package com.pixel.ohtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pixel.okhttp.HttpClientFactory;
import com.pixel.okhttp.callback.ResultCallback;
import com.pixel.okhttp.manage.HttpClient;
import com.pixel.okhttp.others.ClientEnum;
import com.pixel.okhttp.others.HttpException;

import java.util.HashMap;

/**
 * @author Administrator
 *         <p>
 *         http://blog.csdn.net/itachi85/article/details/51190687
 */
public class MainActivity extends Activity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
    }

    public void doGet(View view) {
        HttpClient httpClient = HttpClientFactory.getClient(ClientEnum.OK_HTTP);
        httpClient.get("https://www.baidu.com", new ResultCallback<String>() {
            @Override
            public void succeed(String result) {
                mTextView.setText(result);
            }
        });
    }

    public void doPost(View view) {
        HttpClient httpClient = HttpClientFactory.getClient(ClientEnum.OK_HTTP);
        httpClient.post("https://www.baidu.com", new HashMap<String, String>(0), new ResultCallback<String>() {
            @Override
            public void succeed(String result) {
                mTextView.setText(result);
            }

            @Override
            public void error(HttpException exception) {
                super.error(exception);
            }
        });
    }

    public void doDownload(View view) {
        HttpClient httpClient = HttpClientFactory.getClient(ClientEnum.OK_HTTP);
        httpClient.download("https://www.baidu.com", "/index.html", new ResultCallback<String>() {
            @Override
            public void succeed(String result) {
                mTextView.setText(result);
            }

            @Override
            public void progress(long progressLength, long contentLength, Boolean complete) {
                mTextView.setText(progressLength + " b");
            }

            @Override
            public void start() {
                super.start();
            }

            @Override
            public void error(HttpException exception) {
                super.error(exception);
            }
        });
    }

}
