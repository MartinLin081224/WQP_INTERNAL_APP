package com.example.a10609516.wqp_internal_app.Basic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.a10609516.wqp_internal_app.R;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    private WebView update_web;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        //取得控制項物件
        initViews();
        WebSettings webSettings = update_web.getSettings();
        webSettings.setSupportZoom(true); //啟用內置的縮放功能
        webSettings.setBuiltInZoomControls(true);//啟用內置的縮放功能
        webSettings.setDisplayZoomControls(false);//讓縮放功能的Button消失
        webSettings.setJavaScriptEnabled(true);//使用JavaScript
        webSettings.setAppCacheEnabled(true);//設置啟動緩存
        webSettings.setSaveFormData(true);//設置儲存
        webSettings.setAllowFileAccess(true);//啟用webview訪問文件數據
        webSettings.setDomStorageEnabled(true);//啟用儲存數據
        update_web.setWebViewClient(new WebViewClient());
        update_web.loadUrl("http://m.wqp-water.com.tw/APP");
        update_web.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        update_web = findViewById(R.id.update_web);
    }
}