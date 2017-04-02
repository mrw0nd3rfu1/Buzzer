package com.example.abhinav.buzzer.Event;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventSearchActivity extends AppCompatActivity {

    //view objects
    EditText editTextName;
    Button buttonAddEvent;
    ListView listViewEvent;
    Toolbar mToolbar;

    //a list to store all the artist from firebase database
    List<EventName> cName;

    DatabaseReference databaseEvent;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        final String clgID = getIntent().getExtras().getString("colgId");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        databaseEvent = FirebaseDatabase.getInstance().getReference().child(clgID).child("Event");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        listViewEvent = (ListView) findViewById(R.id.listViewArtists);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Event Names");

        //list to store artists
        cName = new ArrayList<>();



        listViewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                EventName artist = cName.get(i);
                Intent setup=new Intent(EventSearchActivity.this,EventTimeline.class);
                   setup.putExtra("EventName",artist.getEventName());
                   setup.putExtra("EventId",artist.getEventID());
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
        databaseEvent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 cName.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    EventName artist = postSnapshot.getValue(EventName.class);
                    cName.add(artist);
                }
                //creating adapter
                EventActivity collegeAdapter = new EventActivity(EventSearchActivity.this, cName);
                //attaching adapter to the listview
                listViewEvent.setAdapter(collegeAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




}