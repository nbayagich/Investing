package edu.oakland.nbayagich.investing;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WebViewActivity extends AppCompatActivity {

    WebView webView = null;
    WebViewBridge bridge = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        webView.loadUrl("file:///android_asset/index.html");
        webView.getSettings().setJavaScriptEnabled(true);

        bridge = new WebViewBridge(webView);
        webView.addJavascriptInterface(bridge, "Android");

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished (WebView view, String url)
            {
                Intent intent = getIntent();
                if (intent.hasExtra("PlotValues")) {
                    HashMap<Integer, String> data = (HashMap<Integer, String>) intent.getSerializableExtra("PlotValues");
                    Map<Integer, String> treeMap = new TreeMap<Integer, String>(data);
                    for (Map.Entry<Integer, String> arr : treeMap.entrySet()){
                        webView.loadUrl("javascript:addDataToChart(" + arr.getKey().toString() + "," + arr.getValue() + ")");
                    }
                }
            }
        });
    }
}
