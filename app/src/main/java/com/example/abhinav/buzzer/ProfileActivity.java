package com.example.abhinav.buzzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private String mPost_key = null;
    private String postId=null;
    private int hasImage=1;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private CircleImageView mProfileImage;
    private TextView mNameUser;
    private TextView mCollegeName;
    private TextView mLocation;

    private FirebaseAuth mAuth;

    private FloatingActionButton mHomeSingleRemoveBtn;

    private String post_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Profile");



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();

        mNameUser = (TextView) findViewById(R.id.nameUser);
        mCollegeName = (TextView) findViewById(R.id.nameCollege);
        mLocation = (TextView) findViewById(R.id.nameLocation);
        mProfileImage = (CircleImageView) findViewById(R.id.userPic);


        // Toast.makeText(HomeSingleActivity.this , mPost_key , Toast.LENGTH_SHORT).show();

        mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_name = (String) dataSnapshot.child("name").getValue();
                String post_college_name = (String) dataSnapshot.child("college_name").getValue();
                post_image = (String) dataSnapshot.child("profile_pic").getValue();
                //  hasImage=(Integer) dataSnapshot.child("With_image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_location = (String) dataSnapshot.child("location").getValue();
                postId=(String)dataSnapshot.child("post_id").getValue();
                mNameUser.setText(post_name);
                mCollegeName.setText(post_college_name);
                mLocation.setText(post_location);

                Picasso.with(ProfileActivity.this).load(post_image).into(mProfileImage);

                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    mHomeSingleRemoveBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
