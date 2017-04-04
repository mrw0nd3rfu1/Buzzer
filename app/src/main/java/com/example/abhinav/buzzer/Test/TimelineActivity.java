package com.example.abhinav.buzzer.Test;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.buzzer.R;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TimelineActivity extends ArrayAdapter<TimelinePosts> {
    private Activity context;
    List<TimelinePosts> cname;
    DatabaseReference mData;
    DatabaseReference mDatabaseLike;
    private boolean mProcessLike = false;
    FirebaseAuth mAuth;

    public TimelineActivity(Activity context, List<TimelinePosts> artists) {
        super(context , R.layout.test_home, artists);
        this.context = context;
        this.cname = artists;
    }

    public void addListItemToAdapter(List<TimelinePosts> list){
        cname.addAll(list);
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.test_home, null, true);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");



        TextView textViewEvent = (TextView) listViewItem.findViewById(R.id.post_event);
        final TextView textViewUser = (TextView) listViewItem.findViewById(R.id.postUsername);
        final CircleImageView user_pic = (CircleImageView) listViewItem.findViewById(R.id.user_pic);
        ImageView postImage = (ImageView) listViewItem.findViewById(R.id.post_image);
        TextView postPost = (TextView)listViewItem.findViewById(R.id.post_text);
        ImageButton mLikeButton = (ImageButton) listViewItem.findViewById(R.id.likeButton);



        TimelinePosts name = cname.get(position);
        textViewEvent.setText(name.getEvent());
        postPost.setText(name.getPost());
        textViewUser.setText(name.getUsername());
        Picasso.with(context).load(name.getProfile_pic()).into(user_pic);
        Picasso.with(context).load(name.getImage()).into(postImage);


   /*     mLikeButton.setOnClickListener(new View.OnClickListener() {
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
        });  */

        return listViewItem;
    }


}
