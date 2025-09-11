package com.example.captureui;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    // constructor
    WebAppInterface(Context context) {
        mContext = context;
    }

    // this function can be called from JavaScript
    @JavascriptInterface
    public void IDfyCaptureComplete(String payload) {
        // Here you receive the message from JS
        System.out.println("📩 Message from React: " + payload);

        // Example: show a toast
        Toast.makeText(mContext, "Received: " + payload, Toast.LENGTH_LONG).show();
    }
}