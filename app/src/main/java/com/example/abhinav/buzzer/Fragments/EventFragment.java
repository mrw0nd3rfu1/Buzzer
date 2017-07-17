package com.example.abhinav.buzzer.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.buzzer.Event.EventListActivity;
import com.example.abhinav.buzzer.Event.EventName;
import com.example.abhinav.buzzer.R;
import com.example.abhinav.buzzer.Timeline.MainActivity;
import com.example.abhinav.buzzer.Timeline.PostActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private RecyclerView mEventList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_event , container , false);

        mEventList = (RecyclerView) mMainView.findViewById(R.id.event_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(MainActivity.clgID).child("Event");
        mDatabase.keepSynced(true);

        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<EventName,EventViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<EventName, EventViewHolder>(
                EventName.class,
                R.layout.event_list,
                EventViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, EventName model, final int position) {

                final String list_user_id = getRef(position).getKey();


                mDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String eventName = dataSnapshot.child("eventName").getValue().toString();
                        final String eventID = dataSnapshot.child("eventID").getValue().toString();

                        viewHolder.setName(eventName);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent setup=new Intent(getContext(),PostActivity.class);
                                setup.putExtra("EventName",eventName);
                                setup.putExtra("EventId",eventID);
                                setup.putExtra("colgId", MainActivity.clgID);
                                setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(setup);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mEventList.setAdapter(friendsRecyclerViewAdapter);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

       public void setName(String event){
            TextView eventView = (TextView) mView.findViewById(R.id.textViewName);
           eventView.setText(event);
        }

    }

}
