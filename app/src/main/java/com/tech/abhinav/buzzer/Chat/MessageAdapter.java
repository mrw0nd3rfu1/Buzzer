package com.tech.abhinav.buzzer.Chat;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.abhinav.buzzer.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Abhinav on 29-Jul-17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Activity context;
    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList){
        this.mMessageList = mMessageList;
        this.context = context;
    }
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout , parent ,false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageText ;
        public TextView nameText;
        public CircleImageView profileImage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.name_text_layout) ;
            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_layout);
            mAuth = FirebaseAuth.getInstance();
        }
    }


    @Override
    public void onBindViewHolder(final MessageAdapter.MessageViewHolder holder, int position) {

        String current_user_id = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(position);
        String from_user = c.getFrom();

        DatabaseReference userData = FirebaseDatabase.getInstance().getReference().child("Users");


       if (from_user.equals(current_user_id)){
           userData.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String image = dataSnapshot.child("profile_pic").getValue().toString();
                   String name = dataSnapshot.child("name").getValue().toString();
                   Picasso.with(context).load(image).into(holder.profileImage);
                   holder.nameText.setText(name);
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
          /*  holder.messageText.setBackgroundColor(Color.WHITE);
            holder.messageText.setTextColor(Color.BLACK); */

        }else {
           userData.child(from_user).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String image = dataSnapshot.child("profile_pic").getValue().toString();
                   String name = dataSnapshot.child("name").getValue().toString();
                   holder.nameText.setText(name);
                   Picasso.with(context).load(image).into(holder.profileImage);

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
           /* holder.messageText.setBackgroundResource(R.drawable.message_text_backgroud);
            holder.messageText.setTextColor(Color.WHITE); */
        }
        holder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


}
