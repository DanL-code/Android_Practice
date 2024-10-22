package com.example.listapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int myTheme = sharedPreferences.getInt("MyTheme", 0);
        if (myTheme == 0) {
            setTheme(R.style.Theme_ListApp);
        } else if (myTheme == 1) {
            setTheme(R.style.theme_2);
        } else if (myTheme == 2) {
            setTheme(R.style.theme_3);
        } else if (myTheme == 3) {
            setTheme(R.style.theme_4);
        } else if (myTheme == 4) {
            setTheme(R.style.theme_5);
        }

        controller = new Controller(this);
    }

    public void onBackPressed() {
        Log.d("ONBACKPRESSED", "Calling controller on back..");
        controller.onBackPress();
    }
}