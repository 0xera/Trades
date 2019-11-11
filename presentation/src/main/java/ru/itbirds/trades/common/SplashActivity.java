package ru.itbirds.trades.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;

import ru.itbirds.trades.R;
import ru.itbirds.trades.ui.SingleActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, SingleActivity.class));
        finish();
    }
}
