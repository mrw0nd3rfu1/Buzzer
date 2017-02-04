package com.example.abhinav.buzzer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class YourActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener mAuthListener;
    Toolbar mtoolbar;
    private DatabaseReference mDatabaseLike;
    private Query mQueryCurrentUser;
    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle mToggle;
    private boolean mProcessLike = false;
    FirebaseRecyclerAdapter<Home, HomeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Home, HomeViewHolder>(

            Home.class,
            R.layout.home_row,
            HomeViewHolder.class,
            mQueryCurrentUser


    ) {
        @Override
        protected void populateViewHolder(HomeViewHolder viewHolder, Home model, int position) {

            final String post_key = getRef(position).getKey();

            viewHolder.setEvent(model.getEvent());
            viewHolder.setPost(model.getPost());
            viewHolder.setImage(getApplicationContext(), model.getImage());
            viewHolder.setUsername(model.getUsername());

            viewHolder.setLikeButton(post_key);

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(MainActivity.this , "You clicked a view" , Toast.LENGTH_SHORT).show();

                    Intent singleHomeIntent = new Intent(YourActivity.this, HomeSingleActivity.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);


        mAuth = FirebaseAuth.getInstance();

        mtoolbar = (Toolbar) findViewById(R.id.nav_actionBar);
        setSupportActionBar(mtoolbar);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");

        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Post");
        mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("uid").equalTo(currentUserId);


        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);

        RecyclerView mHomePage = (RecyclerView) findViewById(R.id.Home_Page);
        mHomePage.setHasFixedSize(true);
        mHomePage.setLayoutManager(new LinearLayoutManager(this));
        mHomePage.setAdapter(firebaseRecyclerAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }



        if (item.getItemId() == R.id.action_logout) {

            logout();
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

    }
}
