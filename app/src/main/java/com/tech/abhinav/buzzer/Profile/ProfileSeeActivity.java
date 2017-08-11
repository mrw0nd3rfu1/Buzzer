package com.tech.abhinav.buzzer.Profile;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.tech.abhinav.buzzer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileSeeActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private String mPost_key = null;
    private String postId=null;
    private int hasImage=1;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private CircleImageView mProfileImage;
    private TextView mNameUser;
    private TextView mCollegeName;
    private TextView mLocation;
    private FloatingActionButton mFab;
    private ImageView mCollegeImage;
    private DatabaseReference mDatabaseCollege;



    private String post_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_see);

        final String clgID = getIntent().getExtras().getString("colgId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Profile");

        mPost_key = getIntent().getExtras().getString("home_id");

        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post").child(mPost_key);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseCollege = FirebaseDatabase.getInstance().getReference().child("College");


        mNameUser = (TextView) findViewById(R.id.nameUser);
        mCollegeName = (TextView) findViewById(R.id.nameCollege);
        mLocation = (TextView) findViewById(R.id.nameLocation);
        mProfileImage = (CircleImageView) findViewById(R.id.userPic);
        mCollegeImage = (ImageView) findViewById(R.id.college_pic);
        mFab = (FloatingActionButton) findViewById(R.id.fab);




        // Toast.makeText(HomeSingleActivity.this , mPost_key , Toast.LENGTH_SHORT).show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String c = (String) dataSnapshot.child("uid").getValue();


                mDatabaseUser.child(c).addValueEventListener(new ValueEventListener() {
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

                        Picasso.with(ProfileSeeActivity.this).load(post_image).into(mProfileImage);

                        String photo = (String) dataSnapshot.child("CollegeId").getValue();
                        mDatabaseCollege.child(photo).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String photo_college = (String) dataSnapshot.child("Image").getValue();
                                Picasso.with(ProfileSeeActivity.this).load(photo_college).into(mCollegeImage);

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



     /*   mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String c = (String) dataSnapshot.child("uid").getValue();


                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mainIntent = new Intent(ProfileSeeActivity.this , MessageListActivity.class);
                        mainIntent.putExtra("profileName", c);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

    }

}
