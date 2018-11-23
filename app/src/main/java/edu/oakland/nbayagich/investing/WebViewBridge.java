package edu.oakland.nbayagich.investing;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewBridge {
    WebView webView = null;

    public WebViewBridge(WebView webView) {
        this.webView = webView;
    }
}
