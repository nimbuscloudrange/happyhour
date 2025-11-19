package com.happyhour.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private WebView webView;
    private AdView adView;
    private GeolocationPermissions.Callback pendingGeoCallback;
    private String pendingGeoOrigin;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // AdMob initialization complete
            }
        });
        
        // Get WebView and AdView from layout
        webView = findViewById(R.id.webView);
        adView = findViewById(R.id.adView);
        
        // Load banner ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        
        // Enable DOM storage
        webView.getSettings().setDomStorageEnabled(true);
        
        // Enable database storage
        webView.getSettings().setDatabaseEnabled(true);
        
        // Allow file access
        webView.getSettings().setAllowFileAccess(true);
        
        // Enable geolocation
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setGeolocationDatabasePath(getFilesDir().getPath());
        
        // Allow universal access from file URLs (for local HTML loading external resources)
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        
        // Enable mixed content (HTTP/HTTPS)
        webView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        
        // Enable cache
        webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        
        // Enable network access
        webView.getSettings().setBlockNetworkLoads(false);
        
        // Add JavaScript interface for bidirectional communication
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        
        // Set WebViewClient to handle page navigation
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Allow loading URLs within the app
                return false;
            }
            
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // Allow loading resources from the web
                return super.shouldInterceptRequest(view, request);
            }
            
            @Override
            public void onReceivedError(WebView view, android.webkit.WebResourceRequest request, android.webkit.WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Log errors for debugging
                android.util.Log.e("WebView", "Error loading: " + request.getUrl() + " - " + error.getDescription());
            }
        });
        
        // Handle geolocation permission prompts from WebView (HTML navigator.geolocation)
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (hasLocationPermission()) {
                    callback.invoke(origin, true, false);
                } else {
                    pendingGeoCallback = callback;
                    pendingGeoOrigin = origin;
                    requestLocationPermission();
                }
            }
        });
        
        // Load the HTML5 UI
        webView.loadUrl("file:///android_asset/index.html");
        
        // Ensure we have location permissions for "Use my location"
        if (!hasLocationPermission()) {
            requestLocationPermission();
        }
    }

    @Override
    public void onBackPressed() {
        // Handle back button - go back in WebView history if possible
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }
    
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    /**
     * JavaScript interface for communication between HTML5 and Android
     */
    public class WebAppInterface {
        /**
         * Called from JavaScript to show a toast message
         */
        @JavascriptInterface
        public void showToast(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        /**
         * Called from JavaScript to get data from Android
         */
        @JavascriptInterface
        public String getDeviceInfo() {
            return "Android Device - " + android.os.Build.MODEL;
        }

        /**
         * Called from JavaScript to send data to Android
         */
        @JavascriptInterface
        public void receiveData(String data) {
            // Process data received from HTML5
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Received: " + data, Toast.LENGTH_SHORT).show();
            });
        }

        /**
         * Called from JavaScript to log errors
         */
        @JavascriptInterface
        public void logError(String error) {
            android.util.Log.e("WebViewJS", error);
        }

        /**
         * Called from JavaScript to make API requests (proxy through Android)
         */
        @JavascriptInterface
        public void makeApiRequest(String url, String callbackId) {
            // This allows Android to make the request and return data to JS
            new Thread(() -> {
                try {
                    java.net.URL apiUrl = new java.net.URL(url);
                    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) apiUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    
                    int responseCode = connection.getResponseCode();
                    if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                        java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        
                        final String jsonResponse = response.toString();
                        // Pass JSON directly - evaluateJavascript can handle it
                        final String escapedCallbackId = callbackId.replace("'", "\\'");
                        runOnUiThread(() -> {
                            // Use a safer method to pass JSON
                            String jsCode = "try { " +
                                "var data = " + jsonResponse + "; " +
                                "window.handleApiResponse('" + escapedCallbackId + "', data); " +
                                "} catch(e) { " +
                                "window.handleApiError('" + escapedCallbackId + "', 'JSON parse error: ' + e.message); " +
                                "}";
                            webView.evaluateJavascript(jsCode, null);
                        });
                    } else {
                        final String error = "HTTP Error: " + responseCode;
                        runOnUiThread(() -> {
                            webView.evaluateJavascript(
                                "window.handleApiError('" + callbackId + "', '" + error + "');",
                                null
                            );
                        });
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    final String error = (e.getMessage() != null ? e.getMessage() : e.toString())
                        .replace("'", "\\'")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r");
                    runOnUiThread(() -> {
                        webView.evaluateJavascript(
                            "window.handleApiError('" + callbackId + "', '" + error + "');",
                            null
                        );
                    });
                }
            }).start();
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
            LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (granted) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                if (pendingGeoCallback != null && pendingGeoOrigin != null) {
                    pendingGeoCallback.invoke(pendingGeoOrigin, true, false);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                if (pendingGeoCallback != null && pendingGeoOrigin != null) {
                    pendingGeoCallback.invoke(pendingGeoOrigin, false, false);
                }
            }
            pendingGeoCallback = null;
            pendingGeoOrigin = null;
        }
    }
}

