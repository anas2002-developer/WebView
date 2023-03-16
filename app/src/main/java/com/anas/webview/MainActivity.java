package com.anas.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView vWB;
    ProgressBar ProgressBar;

    String url = "https://www.youtube.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vWB=findViewById(R.id.vWV);
        ProgressBar=findViewById(R.id.ProgressBar);
        vWB.getSettings().setJavaScriptEnabled(true);
        vWB.loadUrl(url);

        //for setting new urls in your old view
        vWB.setWebViewClient(new WebViewClient(){

            //when page is loading
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            //when page is loaded
            @Override
            public void onPageFinished(WebView view, String url) {
                ProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        vWB.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.setDescription("Downloading file...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(getApplicationContext(), "Downloading Complete", Toast.LENGTH_SHORT).show();
                }
            };
        });

    }

    private void checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onBackPressed() {
        if (vWB.canGoBack()){
            vWB.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}