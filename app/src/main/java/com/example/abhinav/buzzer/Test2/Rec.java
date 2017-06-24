package com.example.abhinav.buzzer.Test2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhinav.buzzer.R;
import com.example.abhinav.buzzer.Test2.Question;
import com.example.abhinav.buzzer.Test2.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rec extends AppCompatActivity {
    private DatabaseReference mDatabase;
    final List<Question> questionList = new ArrayList<>();
    private RecyclerViewAdapter mAdapter;
    private int currentPage = 0;
    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    final int[] count = {0};
    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private String lastKey = null;
    static String clgID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec);

        clgID = getIntent().getExtras().getString("colgId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAdapter = new RecyclerViewAdapter(this ,questionList);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreData();
            }
        });
        loadData();
    }


    private void loadData() {

        final String[] post_key = {null};
        mDatabase.limitToFirst(TOTAL_ITEM_EACH_LOAD)
                    .startAt(lastKey)
                    .orderByChild("post_id")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChildren()) {
                                lastKey = "last";
                                currentPage--;

                            }
                            post_key[0] = lastKey;

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Question question = data.getValue(Question.class);

                                questionList.add(question);
                                lastKey = String.valueOf(question.getPost_id());
                                mAdapter.notifyDataSetChanged();


                            }

                           if (lastKey.equalsIgnoreCase(post_key[0])){
                                count[0]++;
                            }

                            mProgressBar.setVisibility(RecyclerView.GONE);
                         }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mProgressBar.setVisibility(RecyclerView.GONE);
                        }
                    });

    }

    private void loadMoreData(){
        currentPage++;
        if (count[0] <= 0){
        loadData();
        mProgressBar.setVisibility(RecyclerView.VISIBLE);}
    }
}