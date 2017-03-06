package com.example.abhinav.buzzer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollegeListActivity extends AppCompatActivity {

    //view objects
    EditText editTextName;
    Button buttonAddCollege;
    ListView listViewCollege;
    Toolbar mToolbar;

    //a list to store all the artist from firebase database
    List<CollegeName> cName;

    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

         databaseArtists = FirebaseDatabase.getInstance().getReference().child("College");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        listViewCollege = (ListView) findViewById(R.id.listViewArtists);
        buttonAddCollege = (Button) findViewById(R.id.buttonAddCollege);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("College Names");

        //list to store artists
        cName = new ArrayList<>();


        buttonAddCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addArtist();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
         databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 cName.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CollegeName artist = postSnapshot.getValue(CollegeName.class);
                    cName.add(artist);
                }

                //creating adapter
                CollegeActivity artistAdapter = new CollegeActivity(CollegeListActivity.this, cName);
                //attaching adapter to the listview
                listViewCollege.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addArtist() {
        String name = editTextName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            CollegeName artist = new CollegeName(id ,name);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "College added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}