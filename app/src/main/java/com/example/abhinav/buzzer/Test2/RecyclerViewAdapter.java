package com.example.abhinav.buzzer.Test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.buzzer.Comment.CommentListActivity;
import com.example.abhinav.buzzer.Profile.ProfileSeeActivity;
import com.example.abhinav.buzzer.R;
import com.example.abhinav.buzzer.Test2.Question;
import com.example.abhinav.buzzer.Timeline.HomeSingleActivity;
import com.example.abhinav.buzzer.Timeline.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Question> mQuestionList;
    private Activity context;
    private boolean mProcessLike=false;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEvent, textViewUser, postPost;
        CircleImageView user_pic;
        ImageView postImage;
        ImageButton mLikeButton;
        ImageButton mCommentButton;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        View mView;

        MyViewHolder(View view) {
            super(view);
            mView = itemView;
            textViewEvent = (TextView) view.findViewById(R.id.post_event);
            textViewUser = (TextView) view.findViewById(R.id.postUsername);
            user_pic = (CircleImageView) view.findViewById(R.id.user_pic);
            postImage = (ImageView) view.findViewById(R.id.post_image);
            postPost = (TextView) view.findViewById(R.id.post_text);
            mLikeButton = (ImageButton) view.findViewById(R.id.likeButton);
            mCommentButton = (ImageButton) view.findViewById(R.id.commentButton);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
            mDatabaseLike.keepSynced(true);


        }
    }

    RecyclerViewAdapter(List<Question> mQuestionList) {
        this.mQuestionList = mQuestionList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Question question = mQuestionList.get(position);
        DatabaseReference mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        holder.textViewEvent.setText(question.getEvent());
        holder.postPost.setText(question.getPost());
        holder.textViewUser.setText(question.getUsername());

        Picasso.with(context).load(question.getProfile_pic()).into(holder.user_pic);
        Picasso.with(context).load(question.getImage()).into(holder.postImage);

        mDatabaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(question.getPost_id()).hasChild(mAuth.getCurrentUser().getUid())) {

                    holder.mLikeButton.setImageResource(R.mipmap.ic_launcher);
                } else {
                    holder.mLikeButton.setImageResource(R.mipmap.ic_launcher_unlike);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
                final FirebaseAuth mAuth= FirebaseAuth.getInstance();
                mProcessLike = true;
                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mProcessLike) {

                            if (dataSnapshot.child(question.getPost_id()).hasChild(mAuth.getCurrentUser().getUid())) {

                                mDatabaseLike.child(question.getPost_id()).child(mAuth.getCurrentUser().getUid()).removeValue();
                                mProcessLike = false;

                            } else {
                                mDatabaseLike.child(question.getPost_id()).child(mAuth.getCurrentUser().getUid()).setValue("Random Value");
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                Intent singleHomeIntent = new Intent(context, HomeSingleActivity.class);
                singleHomeIntent.putExtra("home_id", question.getPost_id());
                singleHomeIntent.putExtra("colgId", Rec.clgID);
                context.startActivity(singleHomeIntent);
            }
        });

        holder.mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, CommentListActivity.class);
                commentIntent.putExtra("home_id", question.getPost_id());
                commentIntent.putExtra("colgId", Rec.clgID);
                commentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(commentIntent);
            }
        });

        holder.user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(context, ProfileSeeActivity.class);
                profileIntent.putExtra("home_id", question.getPost_id());
                profileIntent.putExtra("colgId", Rec.clgID);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(profileIntent);
            }
        });

        holder.textViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(context, ProfileSeeActivity.class);
                profileIntent.putExtra("home_id", question.getPost_id());
                profileIntent.putExtra("colgId", Rec.clgID);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(profileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }
}