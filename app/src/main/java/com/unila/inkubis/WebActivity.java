package com.unila.inkubis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.unila.inkubis.R;

public class WebActivity extends AppCompatActivity {

    WebView wvContent;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("nama_tutorial"));

        wvContent = (WebView) findViewById(R.id.webview);
        wvContent.loadUrl(intent.getStringExtra("link_tutorial"));
//        wvContent.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>", "text/html", "UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
