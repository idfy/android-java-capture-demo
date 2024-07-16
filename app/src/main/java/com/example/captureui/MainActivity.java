package com.example.captureui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {

    private static final String[] optionsList = {"Chrome Custom Tabs", "Android WebView"};

    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textVersion = findViewById(R.id.textVersion);
        textVersion.setText(getString(R.string.version_name, BuildConfig.VERSION_NAME));
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            // Check if all required permissions are granted
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                openInWebview();
            } else {
                // Permissions denied, show error
                Toast.makeText(this, "Permissions denied. Please grant the permissions to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Called when user clicks GO */
    public void openUrl(View view) {
        EditText editText = findViewById(R.id.urlEditText);
        String url = editText.getText().toString();
        boolean isURLValid = URLUtil.isValidUrl(url);
        if (!isURLValid)
        {
            Toast.makeText(getApplicationContext(), R.string.invalid_url_message, Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.option_select_title);
            alertDialogBuilder.setItems(optionsList, (dialog, which) -> {
                if (which == 0) {
                    openInCustomTabs();
                } else {
                    openInWebview();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    /** Called when user selects Chrome Custom Tabs */
    private void openInCustomTabs()
    {
        EditText editText = findViewById(R.id.urlEditText);
        String url = editText.getText().toString();

        CustomTabsIntent.Builder customTabsIntentBuilder = new CustomTabsIntent.Builder();
        customTabsIntentBuilder.setShowTitle(false);
        customTabsIntentBuilder.setUrlBarHidingEnabled(true);
        CustomTabsIntent customTabsIntent = customTabsIntentBuilder.build();
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(url), new WebViewFallback());
    }

    private boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Called when user selects WebView */
    private void openInWebview()
    {
        if(!hasPermissions(REQUIRED_PERMISSIONS)) {
            requestPermissions();
            return;
        }
        EditText editText = findViewById(R.id.urlEditText);
        String url = editText.getText().toString();

        WebViewFallback webViewFallback = new WebViewFallback();
        webViewFallback.openUri(this, Uri.parse(url));
    }
}
