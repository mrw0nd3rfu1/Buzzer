package com.example.abhinav.buzzer.Message;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.abhinav.buzzer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageActivity extends ArrayAdapter<MessagePosts> {
    private Activity context;
    List<MessagePosts> cname;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    public MessageActivity(Activity context, List<MessagePosts> artists) {
        super(context , R.layout.message_layout, artists);
        this.context = context;
        this.cname = artists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.message_layout, null, true);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Users");


        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        final TextView textViewUser = (TextView) listViewItem.findViewById(R.id.textViewUser);
        final CircleImageView user_pic = (CircleImageView) listViewItem.findViewById(R.id.userPic);
        final TextView messageTime = (TextView)listViewItem.findViewById(R.id.message_time);



        MessagePosts name = cname.get(position);
        textViewName.setText(name.getuserName());
        mData.child(name.getComment()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String c = (String) dataSnapshot.child("name").getValue();
                textViewUser.setText(c);
                String d = (String) dataSnapshot.child("profile_pic").getValue();
                Picasso.with(context).load(d).into(user_pic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                name.getMessageTime()));

        return listViewItem;
    }


}
