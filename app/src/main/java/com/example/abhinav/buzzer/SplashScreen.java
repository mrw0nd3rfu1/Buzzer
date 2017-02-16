package com.example.abhinav.buzzer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i=0;i<2000;i++){
        }

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
