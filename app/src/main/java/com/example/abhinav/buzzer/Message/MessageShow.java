package com.example.abhinav.buzzer.Message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.abhinav.buzzer.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageShow extends AppCompatActivity {

    //view objects
    EditText editTextName;
    Button buttonAddEvent;
    ListView listViewEvent;
    Toolbar mToolbar;
    DatabaseReference databaseMessage;
    FirebaseAuth mAuth;

    //a list to store all the artist from firebase database
    List<MessagePosts> cName;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);

        final String clgID = getIntent().getExtras().getString("colgId");

        mAuth = FirebaseAuth.getInstance();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        databaseMessage = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Messages");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        listViewEvent = (ListView) findViewById(R.id.listViewArtists);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Messages");

        //list to store artists
        cName = new ArrayList<>();



        listViewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                MessagePosts artist = cName.get(i);
                Intent setup=new Intent(MessageShow.this,MessageListActivity.class);
                setup.putExtra("profileName",artist.getMessageID());
                setup.putExtra("colgId", clgID);
                setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(setup);
                //starting the activity with intent
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cName.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MessagePosts artist = postSnapshot.getValue(MessagePosts.class);
                    cName.add(artist);
                }
                //creating adapter
                MessageActivity collegeAdapter = new MessageActivity(MessageShow.this, cName);
                //attaching adapter to the listview
                listViewEvent.setAdapter(collegeAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




}