package com.belantar.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView secretV;
    String SECRET="http://belantar.com";
    WebSettings webSettings;
    boolean conn = false;
    BottomNavigationView botNav;
    String currentSecret;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Belantar");
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        try{
            botNav = findViewById(R.id.bn_main);
            currentSecret = SECRET;

            secretV = findViewById(R.id.webview);
            progressBar = findViewById(R.id.progress_bar_horizontal);

            conn = false;

            secretV.loadUrl(currentSecret);
            secretV.requestFocus();
            webSettings = secretV.getSettings();
            webSettings.setJavaScriptEnabled(true);
            secretV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            secretV.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
            secretV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            secretV.setScrollbarFadingEnabled(true);
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
                    Log.e("TAGG", error.toString());
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Log.e("TAGG", error.toString());
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    Log.e("TAGG", errorResponse.getStatusCode() +"");
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("TAGG", failingUrl.toString());
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.d("TAGG", "Finish Nih");
                    secretV.loadUrl("javascript:(function() {" +
                            "document.getElementById('navigasi').style.display='none';})()");
                }
            });
            secretV.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    progressBar.setProgress(newProgress);
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("TAGG", newProgress+"");
                    if(newProgress == 100){
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            ImageButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Pencarian");
                    final EditText inputSearch = new EditText(MainActivity.this);
                    inputSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                    alert.setView(inputSearch);
                    alert.setPositiveButton("Cari", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentSecret = SECRET+"search?q=" + inputSearch.getText().toString().trim() + "&max-results=8&m=1";
                            secretV.loadUrl(currentSecret);
                        }
                    });
                    alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
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
        } catch (IllegalArgumentException ex){
            Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        } catch (Exception ex){
            Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(secretV.canGoBack()){
            if(conn){
                secretV.goBack();
            }else{
                secretV.goBack();
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
            currentSecret = SECRET;
            secretV.loadUrl(currentSecret);
        }
        if(id == R.id.nav_tips_cat){
            currentSecret = SECRET+"search/label/TIPS";
            secretV.loadUrl(currentSecret);
        }
        if(id == R.id.nav_cat_knowledge){
            currentSecret=SECRET+"search/label/PENGETAHUAN";
            secretV.loadUrl(currentSecret);
        }
        if(id == R.id.nav_cat_story){
            currentSecret = SECRET+"search/label/CERITA%20KUCING";
            secretV.loadUrl(currentSecret);
        }
        if(id == R.id.nav_about){
            Intent it =new Intent(MainActivity.this,AboutActivity.class);
            startActivity(it);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
