package com.example.abhinav.buzzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();



        Thread myThread = new Thread(){
            @Override
            public void run() {

                try {
                    sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                try{
                    final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String clgID = (String) dataSnapshot.child("CollegeId").getValue();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("colgId", clgID);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });}
                    catch(NullPointerException e)
                    {
                        Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                        startActivity(intent);
                    }

            }
        };
        myThread.start();
    }
}
