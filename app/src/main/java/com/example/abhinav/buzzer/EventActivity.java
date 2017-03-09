package com.example.abhinav.buzzer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EventActivity extends AppCompatActivity {

    NavigationView navigationView;
    Toolbar mtoolbar;
    FloatingActionButton mfab;
    private RecyclerView mHomePage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth mAuth;
    private Query orderData;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessLike = false;
    private LinearLayoutManager mLayoutManager;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7607893686244125~3347511713");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(EventActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mfab = (FloatingActionButton) findViewById(R.id.fab);

        mtoolbar = (Toolbar) findViewById(R.id.nav_actionBar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setTitle("Event");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("post_id");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        orderData=mDatabase.orderByChild("post_id");
        orderData.keepSynced(true);

        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);


        mHomePage = (RecyclerView) findViewById(R.id.Home_Page);
        mHomePage.setHasFixedSize(true);
        mHomePage.setLayoutManager(new LinearLayoutManager(this));

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(EventActivity.this, PostActivity2.class);
                postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(postIntent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthListener);

        final FirebaseRecyclerAdapter<Home, HomeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Home, HomeViewHolder>(

                Home.class,
                R.layout.home_row,
                HomeViewHolder.class,
                orderData


        ) {
            @Override
            protected void populateViewHolder(HomeViewHolder viewHolder, Home model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setPost(model.getPost());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());

                viewHolder.setLikeButton(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this , "You clicked a view" , Toast.LENGTH_SHORT).show();

                        Intent singleHomeIntent = new Intent(EventActivity.this, HomeSingleActivity.class);
                        singleHomeIntent.putExtra("CollegeId",getIntent().getStringExtra("CollegeId"));
                        singleHomeIntent.putExtra("home_id", post_key);
                        startActivity(singleHomeIntent);
                    }
                });

                viewHolder.mLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessLike = true;


                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessLike) {

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;

                                    } else {
                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random Value");
                                        mProcessLike = false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
            }
        };

        mLayoutManager = new LinearLayoutManager(EventActivity.this);
        mHomePage.setLayoutManager(mLayoutManager);
        mHomePage.setAdapter(firebaseRecyclerAdapter);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_logout){

            logout();
        }

        if (item.getItemId() == R.id.action_profile){
            Intent profileIntent = new Intent(EventActivity.this, ProfileActivity.class);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ImageButton mLikeButton;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public HomeViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikeButton = (ImageButton) mView.findViewById(R.id.likeButton);

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
        }

        public void setLikeButton(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                        mLikeButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
                    } else {
                        mLikeButton.setImageResource(R.mipmap.ic_thumb_up_white_24dp);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        public void setPost(String post){
            TextView post_text = (TextView) mView.findViewById(R.id.post_text);
            post_text.setText(post);

        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setUsername(String username){
            TextView post_username = (TextView) mView.findViewById(R.id.postUsername);
            post_username.setText(username);
        }

    }
}