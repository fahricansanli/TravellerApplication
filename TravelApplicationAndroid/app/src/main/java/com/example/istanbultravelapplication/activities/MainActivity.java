package com.example.istanbultravelapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Window;

import android.os.Bundle;

import com.facebook.stetho.Stetho;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        Stetho.initializeWithDefaults(this);
        SharedPreferences sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);
        if(sharedPref.contains("isUserLogInned")){
            Intent i = new Intent(this,DistrictActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
