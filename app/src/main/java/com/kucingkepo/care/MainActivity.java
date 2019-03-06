package com.kucingkepo.care;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView secretV;
    String SECRET="https://www.kucingkepo.com/";
    WebSettings webSettings;
    private AdView adView;
    boolean conn = false;
    BottomNavigationView botNav;
    String currentSecret;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("KUCING KEPO");
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        botNav = (BottomNavigationView) findViewById(R.id.bn_main);
        currentSecret = SECRET;

        secretV = (WebView) findViewById(R.id.webview);
        adView = (AdView) findViewById(R.id.adView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_horizontal);

        conn = false;

        MobileAds.initialize(this,
                "ca-app-pub-9043865407906531~4701637852");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        secretV.loadUrl(currentSecret);
        secretV.requestFocus();
        webSettings = secretV.getSettings();
        webSettings.setJavaScriptEnabled(true);
        secretV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        secretV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        secretV.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
        secretV.getSettings().setAppCacheEnabled(true);
        secretV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        secretV.setScrollbarFadingEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            secretV.evaluateJavascript("enable();", null);
        } else {
            secretV.loadUrl("javascript:enable();");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            secretV.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            secretV.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        secretV.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                return;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                return;
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                return;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                return;
            }
        });
        secretV.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.setVisibility(View.VISIBLE);
                if(newProgress == 100){
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Search");
                final EditText inputSearch = new EditText(MainActivity.this);
                inputSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(inputSearch);
                alert.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentSecret = SECRET+"search?q=" + inputSearch.getText().toString().trim() + "&max-results=8&m=1";
                        secretV.loadUrl(currentSecret);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(!conn){
                    Toast.makeText(MainActivity.this,"Jaringan di matikan.",Toast.LENGTH_LONG).show();
                }
                if(id == R.id.navv_home){
                    currentSecret = SECRET;
                    secretV.loadUrl(currentSecret);
                    return true;
                }
                if(id == R.id.navv_tips_cat){
                    currentSecret = SECRET+"search/label/TIPS";
                    secretV.loadUrl(currentSecret);
                    return true;
                }
                if(id == R.id.navv_cat_knowledge){
                    currentSecret=SECRET+"search/label/PENGETAHUAN";
                    secretV.loadUrl(currentSecret);
                    return true;
                }
                if(id == R.id.navv_cat_story){
                    currentSecret = SECRET+"search/label/CERITA%20KUCING";
                    secretV.loadUrl(currentSecret);
                    return true;
                }
                if(id == R.id.navv_refresh){
                    secretV.loadUrl(currentSecret);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(secretV.canGoBack()){
            if(conn){
                secretV.goBack();
            }else{
                return;
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!NetworkChangeReceiver.isConnected(getApplicationContext())){
                conn = false;
                Toast.makeText(MainActivity.this,"Jaringan di matikan.",Toast.LENGTH_LONG).show();
            }else {
                conn = true;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            secretV.loadUrl(currentSecret);
            return true;
        }
        if(id == R.id.nav_tips_cat){
            currentSecret = SECRET+"search/label/TIPS";
            secretV.loadUrl(currentSecret);
            return true;
        }
        if(id == R.id.nav_cat_knowledge){
            currentSecret=SECRET+"search/label/PENGETAHUAN";
            secretV.loadUrl(currentSecret);
            return true;
        }
        if(id == R.id.nav_cat_story){
            currentSecret = SECRET+"search/label/CERITA%20KUCING";
            secretV.loadUrl(currentSecret);
            return true;
        }
        if(id == R.id.nav_about){
            Intent it =new Intent(MainActivity.this,AboutActivity.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
