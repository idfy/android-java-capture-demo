package com.example.captureui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

public class IDfyWebView extends WebView {
    public IDfyWebView(Context context) {
        super(context);
    }

    public IDfyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IDfyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility != View.GONE) super.onWindowVisibilityChanged(View.VISIBLE);
    }
}
