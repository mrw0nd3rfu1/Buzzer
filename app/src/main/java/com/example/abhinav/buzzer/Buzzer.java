package com.example.abhinav.buzzer;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

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

