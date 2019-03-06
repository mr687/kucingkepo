package com.kucingkepo.care;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AboutActivity extends AppCompatActivity {
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("Tentang Kucing Kepo");

        adView = (AdView) findViewById(R.id.adView);

        MobileAds.initialize(this,
                "ca-app-pub-9043865407906531~4701637852");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse("whatsapp://send?phone=6281332630202&text=Hi%20kucing%20kepo")));
            }
        });
    }
}
