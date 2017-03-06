package com.example.abhinav.buzzer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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

    Toolbar mtoolbar;
    FloatingActionButton mfab;
    InterstitialAd mInterstitialAd;
    String LIST_STATE_KEY = "";
    private static final int HEADER_VIEW = 2;
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
    private InterstitialAd interstitial;
    private boolean isUserClickedBackButton = false;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private CircleImageView mProfileImage;
    private TextView mNameUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7607893686244125~3347511713");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
     //   mAdView.loadAd(adRequest);

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
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
        collapsingToolbarLayout.setTitle("Home");
        //  mProfileImage = (CircleImageView) findViewById(R.id.profile_pic);
        mNameUser = (TextView) findViewById(R.id.user_name);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        orderData = mDatabase.orderByChild("post_id");
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        orderData.keepSynced(true);

        mHomePage = (RecyclerView) findViewById(R.id.Home_Page);
        mHomePage.setHasFixedSize(true);
        mHomePage.setLayoutManager(new LinearLayoutManager(this));

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(MainActivity.this, PostActivity.class);
                postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(postIntent);
            }
        });
        checkUserExist();
    }

    /* protected void onSaveInstanceState(Bundle state) {
         super.onSaveInstanceState(state);

         state.putParcelable(LIST_STATE_KEY, layoutManager.onSaveInstanceState());
     }

     protected void onRestoreInstanceState(Bundle state) {
         super.onRestoreInstanceState(state);

         Parcelable listState = state.getParcelable(LIST_STATE_KEY);
     }

     @Override
     protected void onResume() {
         super.onResume();

         if (listState != null) {
             mlayoutManager.onRestoreInstanceState(listState);
         }
     }
 */
    @Override
    public void onBackPressed() {
        if (!isUserClickedBackButton) {
            Toast.makeText(this, "Press Back button again to Exit", Toast.LENGTH_SHORT).show();
            isUserClickedBackButton = true;
        } else {
            super.onBackPressed();
        }

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isUserClickedBackButton = false;
            }
        }.start();
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        // if (interstitial.isLoaded()) {
        //   interstitial.show();
        // }
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
            public int getItemCount() {
                return (super.getItemCount() + 1);
            }

            @Override
            public int getItemViewType(int position) {
                if (position != 0) {
                    Home obj = getItem(position - 1);
                    switch (obj.getHas_image()) {
                        case 0:
                            return 0;
                        case 1:
                            return 1;
                    }
                } else return HEADER_VIEW;
                return super.getItemViewType(position);
            }

            @Override
            public void onBindViewHolder(HomeViewHolder viewHolder, int position) {
                if (getItemViewType(position) == HEADER_VIEW) {
                    //put the code to do things in card here
                } else {
                    Home model = getItem(position - 1);
                    populateViewHolder(viewHolder, model, position - 1);
                }
            }

            @Override
            public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == HEADER_VIEW) {

                    View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                    return new headerView(header);
                } else {
                    switch (viewType) {
                        case 0:
                            View type1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_wihtout_image, parent, false);
                            return new HomeViewHolder(type1);
                        case 1:
                            View type2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row, parent, false);
                            return new HomeViewHolder(type2);
                    }
                }

                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void populateViewHolder(HomeViewHolder viewHolder, Home model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setEvent(model.getEvent());
                viewHolder.setPost(model.getPost());
                if (model.getHas_image() == 1)
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setLikeButton(post_key);
                viewHolder.setProfile_Pic(getApplicationContext(), model.getProfile_pic());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this , "You clicked a view" , Toast.LENGTH_SHORT).show();

                        Intent singleHomeIntent = new Intent(MainActivity.this, HomeSingleActivity.class);
                        Pair<View, String> pair1 = Pair.create(findViewById(R.id.post_image), "myImage");
                        Pair<View, String> pair2 = Pair.create(findViewById(R.id.post_image), "myEvent");
                        Pair<View, String> pair3 = Pair.create(findViewById(R.id.post_image), "myPost");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1, pair2, pair3);
                        singleHomeIntent.putExtra("home_id", post_key);
                        startActivity(singleHomeIntent, optionsCompat.toBundle());
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

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mHomePage.setLayoutManager(mLayoutManager);
        mHomePage.setAdapter(firebaseRecyclerAdapter);


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
                                String post_image = (String) dataSnapshot.child("profile_pic").getValue();
                                //  Picasso.with(MainActivity.this).load(post_image).into(mProfileImage);
                                String post_name = (String) dataSnapshot.child("name").getValue();
                                mNameUser.setText(post_name);
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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_logout) {

            logout();
        }

        if (item.getItemId() == R.id.action_profile) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
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

        HomeViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikeButton = (ImageButton) mView.findViewById(R.id.likeButton);

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

        }


        public void setLikeButton(final String post_key) {
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

        public void setEvent(String event) {
            TextView post_event = (TextView) mView.findViewById(R.id.post_event);
            post_event.setText(event);

        }

        public void setPost(String post) {

            TextView post_text = (TextView) mView.findViewById(R.id.post_text);
            post_text.setText(post);


        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setUsername(String username) {
            TextView post_username = (TextView) mView.findViewById(R.id.postUsername);
            post_username.setText(username);
        }

        public void setProfile_Pic(Context ctx, String image) {
            CircleImageView profile_pic = (CircleImageView) mView.findViewById(R.id.user_pic);
            Picasso.with(ctx).load(image).into(profile_pic);

        }


    }

    public static class headerView extends HomeViewHolder {
        public headerView(View itemView) {
            super(itemView);

        }
    }


}
