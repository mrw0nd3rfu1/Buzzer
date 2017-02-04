package com.example.abhinav.buzzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeSingleActivity extends AppCompatActivity {

    private String mPost_key = null;
    private String postId=null;
    private DatabaseReference mDatabase;
     private StorageReference mStorage;
    private ImageView mHomeSingleImage;
    private TextView mHomeSingleEvent;
    private TextView mHomeSinglePost;
    private TextView mHomeSingleUsername;

    private FirebaseAuth mAuth;

    private Button mHomeSingleRemoveBtn;

    private String post_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_single);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");


        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("home_id");

        mHomeSingleEvent = (TextView) findViewById(R.id.singleHomeEvent);
        mHomeSinglePost = (TextView) findViewById(R.id.singleHomePost);
        mHomeSingleUsername = (TextView) findViewById(R.id.singleHomeUsername);
        mHomeSingleImage = (ImageView) findViewById(R.id.singleHomeImage);

        mHomeSingleRemoveBtn = (Button) findViewById(R.id.removeButton);

       // Toast.makeText(HomeSingleActivity.this , mPost_key , Toast.LENGTH_SHORT).show();

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_event = (String) dataSnapshot.child("event").getValue();
                String post_post = (String) dataSnapshot.child("post").getValue();
                post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_username = (String) dataSnapshot.child("username").getValue();
                postId=(String)dataSnapshot.child("post_id").getValue();
                mHomeSingleEvent.setText(post_event);
                mHomeSinglePost.setText(post_post);
                mHomeSingleUsername.setText(post_username);

                Picasso.with(HomeSingleActivity.this).load(post_image).into(mHomeSingleImage);

                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    mHomeSingleRemoveBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mHomeSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mPost_key).removeValue();
                mStorage= FirebaseStorage.getInstance().getReference().child("Posts/"+postId);
                mStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomeSingleActivity.this,"Removed Succesfully",Toast.LENGTH_SHORT).show();                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(HomeSingleActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                Intent mainIntent = new Intent(HomeSingleActivity.this, MainActivity.class);
                startActivity(mainIntent);

            }
        });

    }


}
