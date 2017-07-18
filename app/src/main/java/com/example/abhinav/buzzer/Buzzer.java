package com.example.abhinav.buzzer;

import android.app.Application;
import android.content.Intent;

import com.example.abhinav.buzzer.Profile.LoginActivity;
import com.example.abhinav.buzzer.Timeline.MainActivity;
import com.example.abhinav.buzzer.Utility.SplashScreen;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Abhinav on 18-Jul-17.
 */

public class Buzzer extends Application {

   @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);





    }
}

