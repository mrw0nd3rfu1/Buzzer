package com.tech.abhinav.buzzer.Chat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tech.abhinav.buzzer.Profile.ProfileSeeActivity;
import com.tech.abhinav.buzzer.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UActivity extends AppCompatActivity {

    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = (Toolbar) findViewById(R.id.u_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUsersList= (RecyclerView) findViewById(R.id.user_list);
        //mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users , UsersViewHolder2> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder2>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder2.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder2 viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setProfile_Pic(getApplicationContext(), model.getProfile_pic());

                final String unique_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(UActivity.this,ProfileSeeActivity.class);
                        profileIntent.putExtra("user_id",unique_id);
                        profileIntent.putExtra("colgId","");
                        startActivity(profileIntent);
                    }
                });
            }
        };
       firebaseRecyclerAdapter.notifyDataSetChanged();
       mUsersList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UsersViewHolder2 extends RecyclerView.ViewHolder{

        View mView;
        public UsersViewHolder2(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView userName = (TextView) mView.findViewById(R.id.user_single_name);
            userName.setText(name);
        }

        public void setStatus(String status){
            TextView userStatus = (TextView) mView.findViewById(R.id.user_single_status);
            userStatus.setText(status);
        }

        public void setProfile_Pic(Context ctx , String thumb_image){
            CircleImageView post_image = (CircleImageView) mView.findViewById(R.id.user_single_image);

            Picasso.with(ctx).load(thumb_image).into(post_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

}
