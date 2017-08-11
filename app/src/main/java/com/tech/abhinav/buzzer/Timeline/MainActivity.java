package com.tech.abhinav.buzzer.Timeline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.abhinav.buzzer.College.CollegeListActivity;
import com.tech.abhinav.buzzer.College.CollegePhotoSelector;
import com.tech.abhinav.buzzer.Event.EventListActivity;
import com.tech.abhinav.buzzer.Profile.LoginActivity;
import com.tech.abhinav.buzzer.Profile.ProfileActivity;
import com.tech.abhinav.buzzer.Profile.SetupActivity;
import com.tech.abhinav.buzzer.R;
import com.tech.abhinav.buzzer.Utility.AboutActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    public static String clgID;
    Toolbar mtoolbar;
    FloatingActionButton mfab;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mCollege;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth mAuth;
    private Query orderData;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AdView mAdView;
    private boolean isUserClickedBackButton = false;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private CircleImageView mProfileImage;
    private ImageView mCollegePic;
    private TextView mNameUser;
    private ImageButton imageView;
    private TextView userClgPic;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSections;
    private TabLayout mTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clgID = getIntent().getExtras().getString("colgId");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.putExtra("colgId", clgID);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        mfab = (FloatingActionButton) findViewById(R.id.fab);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationIcon(null);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("  ");
        mProfileImage = (CircleImageView) findViewById(R.id.profile_pic);
        mCollegePic = (ImageView)findViewById(R.id.college_pic);
        imageView = (ImageButton) findViewById(R.id.imageSelect);
        mNameUser = (TextView) findViewById(R.id.user_name);
        userClgPic = (TextView) findViewById(R.id.user_clg_name);
        mCollege = FirebaseDatabase.getInstance().getReference().child("College").child(clgID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        orderData = mDatabase.orderByChild("post_id").limitToFirst(30);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);
        orderData.keepSynced(true);

        //tabs
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mSections = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSections);

        mTab = (TabLayout) findViewById(R.id.main_tab);
        mTab.setupWithViewPager(mViewPager);

        //checking user exists in college
        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("CollegeId").getValue().equals(clgID))
                    mfab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(MainActivity.this, EventListActivity.class);
                postIntent.putExtra("colgId",clgID);
                postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(postIntent);
            }
        });

        mAuth.addAuthStateListener(mAuthListener);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("colgId",clgID);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(profileIntent);
            }
        });

        checkUserExist();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (!isUserClickedBackButton) {
            Toast.makeText(this, "Press Back button again to Exit", Toast.LENGTH_SHORT).show();
            isUserClickedBackButton = true;
        }
        else
        {
            super.onBackPressed();
        }
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isUserClickedBackButton=false;
            }
        }.start();
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        // if (interstitial.isLoaded()) {
        //   interstitial.show();
        // }
    }

    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {

            final String user_ID = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_ID)) {

                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    } else {
                        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String profile_image = (String) dataSnapshot.child("profile_pic").getValue();
                                Picasso.with(MainActivity.this).load(profile_image).into(mProfileImage);
                                String post_name = (String) dataSnapshot.child("name").getValue();
                                mNameUser.setText(post_name);
                                mCollege.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String college_image = (String) dataSnapshot.child("Image").getValue();
                                        Picasso.with(MainActivity.this).load(college_image).into(mCollegePic);
                                        String college_user_name = (String) dataSnapshot.child("ImagePost").getValue();
                                        userClgPic.setText("Last Uploaded By "+college_user_name);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //  String clg = (String) dataSnapshot.child("CollegeId").getValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        getMenuInflater().inflate(R.menu.college_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {



        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String clgID = getIntent().getExtras().getString("colgId");
                if (dataSnapshot.child("CollegeId").getValue().equals(clgID))
                { if (item.getItemId() == R.id.item_photo){
                    Intent collegeIntent = new Intent(MainActivity.this, CollegePhotoSelector.class);
                    collegeIntent.putExtra("colgId", clgID);
                    collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(collegeIntent); }
                }
                else {
                    Toast.makeText(MainActivity.this , "You are not present in this college" ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (item.getItemId()== R.id.action_college){
            Intent collegeIntent = new Intent(MainActivity.this, CollegeListActivity.class);
            collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(collegeIntent);
        }
        if (item.getItemId() == R.id.action_logout) {

            logout();
        }




        if (item.getItemId() == R.id.action_profile) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);

        }


        if (item.getItemId() == R.id.action_about) {
            final String clgID = getIntent().getExtras().getString("colgId");

            Intent profileIntent = new Intent(MainActivity.this, AboutActivity.class);
            profileIntent.putExtra("colgId", clgID);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }

    }

    private void logout() {
        mAuth.signOut();
    }

}


