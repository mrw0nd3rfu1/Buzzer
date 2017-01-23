package com.example.abhinav.buzzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HomeSingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_single);

        String post_key = getIntent().getExtras().getString("home_id");

        Toast.makeText(HomeSingleActivity.this , post_key , Toast.LENGTH_SHORT).show();
    }
}
