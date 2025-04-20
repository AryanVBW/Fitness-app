package com.example.fitnesstracker;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class WorkoutLibraryActivity extends AppCompatActivity {
    private WebView workoutWebView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String WORKOUT_URL = "https://arise-aditya.mvt.so/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_library);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize WebView
        workoutWebView = findViewById(R.id.workoutWebView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Configure WebView
        WebSettings webSettings = workoutWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // Set WebViewClient to handle page navigation
        workoutWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
                
                // Inject JavaScript to hide popups and unwanted elements
                injectJavaScript();
            }
        });

        // Setup pull-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            workoutWebView.reload();
        });

        // Load the workout website
        workoutWebView.loadUrl(WORKOUT_URL);
    }

    private void injectJavaScript() {
        String jsCode = "javascript:(function() { " +
            "document.querySelectorAll('.popup, .modal, .overlay').forEach(function(e) { e.style.display = 'none'; }); " +
            "document.querySelectorAll('body').forEach(function(e) { e.style.overflow = 'auto'; }); " +
            "document.querySelectorAll('header, footer, .header, .footer').forEach(function(e) { e.style.display = 'none'; }); " +
            "})()";
        workoutWebView.evaluateJavascript(jsCode, null);
    }

    @Override
    public void onBackPressed() {
        if (workoutWebView.canGoBack()) {
            workoutWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (workoutWebView != null) {
            workoutWebView.destroy();
        }
        super.onDestroy();
    }
} 