package com.anas.webview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    WebView vWB;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vWB=findViewById(R.id.vWV);
        ProgressBar=findViewById(R.id.ProgressBar);
        vWB.loadUrl("https://www.google.com/");

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