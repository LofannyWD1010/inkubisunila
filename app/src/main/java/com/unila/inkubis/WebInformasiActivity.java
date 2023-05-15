package com.unila.inkubis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.unila.inkubis.R;

public class WebInformasiActivity extends AppCompatActivity {

    String linkWeb;
    WebView myWebView;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_informasi);

        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra("TITLE"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myWebView  = (WebView) findViewById(R.id.webview);
        loadingBar = (ProgressBar) findViewById(R.id.loading_bar);
        linkWeb = intent.getStringExtra("LINK");

        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loadingBar.setVisibility(View.VISIBLE);
                myWebView.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Uri.parse(url).getHost().equals("blog.sigerdev.com")) {
                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                loadingBar.setVisibility(View.GONE);
                myWebView.setVisibility(View.VISIBLE);
                view.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('navbar navbar-default')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('widget-area col-sm-12 col-md-4')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('comments-area')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('navigation post-navigation')[0].style.display='none'; " +
                        "})()");

            }
        });
        myWebView.loadUrl(linkWeb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
