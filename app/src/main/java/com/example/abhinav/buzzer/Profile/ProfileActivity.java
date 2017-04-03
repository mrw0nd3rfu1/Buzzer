package com.example.abhinav.buzzer.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.buzzer.R;
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
    private DatabaseReference mDatabaseCollege;
    private StorageReference mStorage;
    private CircleImageView mProfileImage;
    private TextView mNameUser;
    private TextView mCollegeName;
    private TextView mLocation;
    private ImageView mCollegeImage;

    private FirebaseAuth mAuth;

    private String post_image;
    private Button edit_Name;
    private  Button edit_Location;


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
        mDatabaseCollege = FirebaseDatabase.getInstance().getReference().child("College");


        mAuth = FirebaseAuth.getInstance();

        mNameUser = (TextView) findViewById(R.id.nameUser);
        mCollegeName = (TextView) findViewById(R.id.nameCollege);
        mLocation = (TextView) findViewById(R.id.nameLocation);
        mProfileImage = (CircleImageView) findViewById(R.id.userPic);
        mCollegeImage = (ImageView) findViewById(R.id.college_pic);
        edit_Name = (Button) findViewById(R.id.edit_Name);
        edit_Location = (Button) findViewById(R.id.edit_Location);


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

                String photo = (String) dataSnapshot.child("CollegeId").getValue();

                mDatabaseCollege.child(photo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String photo_college = (String) dataSnapshot.child("Image").getValue();
                        Picasso.with(ProfileActivity.this).load(photo_college).into(mCollegeImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    edit_Name.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           updateName();
        }
    });
        edit_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     updateLocation();


            }
        });

        mProfileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfilePhotoSelector.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });




    }
    public boolean updateName(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.name_update, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    b.dismiss();
                    mDatabase.child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);
                }
            }
        });
        return true;
    }

    public boolean updateLocation (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.location_update, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dr= mDatabase.child(mAuth.getCurrentUser().getUid()).child("location");
                    dr.setValue(name);
                    b.dismiss();
                }
            }
        });

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

         return true;
    }
}
