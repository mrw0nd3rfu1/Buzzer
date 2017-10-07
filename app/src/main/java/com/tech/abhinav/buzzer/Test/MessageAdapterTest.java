package com.tech.abhinav.buzzer.Test;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.abhinav.buzzer.Comment.CommentListActivity;
import com.tech.abhinav.buzzer.Profile.ProfileSeeActivity;
import com.tech.abhinav.buzzer.R;
import com.tech.abhinav.buzzer.Timeline.HomeSingleActivity;
import com.tech.abhinav.buzzer.Timeline.MainActivity;
import com.tech.abhinav.buzzer.Utility.Home;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Abhinav on 29-Jul-17.
 */

public class MessageAdapterTest extends RecyclerView.Adapter<MessageAdapterTest.MessageViewHolder> {

    private Activity context;
    private List<Home> mMessageList;
    private FirebaseAuth mAuth;

    public MessageAdapterTest(List<Home> mMessageList){
        this.mMessageList = mMessageList;
        this.context = context;
    }
    @Override
    public MessageAdapterTest.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row , parent ,false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton mLikeButton;
        ImageButton mCommentButton;

        CircleImageView mProfileImage;
        TextView mUserName;
        Animation mLike;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;


        public MessageViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mLikeButton = (ImageButton) mView.findViewById(R.id.likeButton);
            mCommentButton = (ImageButton) mView.findViewById(R.id.commentButton);
            mProfileImage = (CircleImageView) mView.findViewById(R.id.user_pic);
            mUserName = (TextView) mView.findViewById(R.id.postUsername);

            //      mLike = AnimationUtils.loadAnimation(baseContext , R.anim.show_layout);


            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
        }
    }


    @Override
    public void onBindViewHolder(final MessageAdapterTest.MessageViewHolder holder, int position) {

        String current_user_id = mAuth.getCurrentUser().getUid();

        final Home c = mMessageList.get(position);
        DatabaseReference userData = FirebaseDatabase.getInstance().getReference().child("Users");

        Picasso.with(context).load(c.getImage()).into(holder.mProfileImage);

            holder.mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(c.getPost_key()).hasChild(mAuth.getCurrentUser().getUid())) {

                        holder.mLikeButton.setImageResource(R.drawable.ic_liked);
                        //        mLikeButton.startAnimation(mLike);

                    } else {
                        holder.mLikeButton.setImageResource(R.drawable.ic_like);
                        //     mLikeButton.startAnimation(mLike);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                Intent singleHomeIntent = new Intent(context, HomeSingleActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                singleHomeIntent.putExtra("home_id", c.getPost_key());
                singleHomeIntent.putExtra("colgId", MainActivity.clgID);
                context.startActivity(singleHomeIntent);
            }
        });

        holder.mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, CommentListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                commentIntent.putExtra("home_id", c.getPost_key());
                commentIntent.putExtra("colgId", MainActivity.clgID);
                context.startActivity(commentIntent);
            }
        });

        holder.mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(context, ProfileSeeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                profileIntent.putExtra("home_id", c.getPost_key());
                profileIntent.putExtra("user_id","");
                profileIntent.putExtra("colgId", MainActivity.clgID);
                context.startActivity(profileIntent);
            }
        });

        holder.mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(context, ProfileSeeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                profileIntent.putExtra("home_id", c.getPost_key());
                profileIntent.putExtra("colgId", MainActivity.clgID);
                context.startActivity(profileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


}
