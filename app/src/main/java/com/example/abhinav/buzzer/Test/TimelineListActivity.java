package com.example.abhinav.buzzer.Test;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abhinav.buzzer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TimelineListActivity extends AppCompatActivity {

    //view objects
    ListView listViewComment;
    Toolbar mToolbar;


    //a list to store all the artist from firebase database
    List<TimelinePosts> cName;

    DatabaseReference databaseComment;
    DatabaseReference databaseUser;
    DatabaseReference database;
    public android.os.Handler mHandler;
    TimelineActivity collegeAdapter;
    View ftView;
    public boolean isLoading = false;

    Query orderData;
    FirebaseAuth mAuth;
    private String mPost_key = null;
    private String lastKey = null;
    private final static int QUERY_LIMIT = 10;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_timeline);

        LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView=li.inflate(R.layout.footer_view, null);
        mHandler = new MyHandler();

        final String clgID = getIntent().getExtras().getString("colgId");
        mPost_key = getIntent().getExtras().getString("home_id");

        database = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");
        orderData = database;
        databaseUser =FirebaseDatabase.getInstance().getReference().child("User");
        mAuth = FirebaseAuth.getInstance();

        //getting views

        listViewComment = (ListView) findViewById(R.id.list_comment);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Testing");

        //list to store artists
        cName = new ArrayList<>();



       /* listViewComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TimelinePosts comment = cName.get(i);
                    if (mAuth.getCurrentUser().getUid().equals(comment.getComment())) {
                        showUpdateDeleteDialog(comment.getCommentID(), comment.getComment());
                    }
                        return true;

                }
        }); */


    }

    @Override
    protected void onStart() {
        super.onStart();

        orderData.limitToLast(QUERY_LIMIT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 cName.clear();
                page++;

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TimelinePosts artist = postSnapshot.getValue(TimelinePosts.class);
                    cName.add(artist);
                    lastKey = String.valueOf(artist.getPost_id());
                }

                Collections.reverse(cName);
                //creating adapter
                collegeAdapter = new TimelineActivity(TimelineListActivity.this, cName);
                //attaching adapter to the listview
                listViewComment.setAdapter(collegeAdapter);
                listViewComment.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        listViewComment.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition()== totalItemCount-1 && listViewComment.getCount()>= QUERY_LIMIT-1 && isLoading==false){
                    isLoading=true;
                    Thread thread = new ThreadGetMoreData();
                    thread.start();
                }
            }
        });
      /*  if (lastKey!= null){
            cName.clear();
             orderData =  database.orderByChild("post_id").endAt(lastKey).limitToLast(QUERY_LIMIT *page);
        }
        else {
             orderData = database.orderByChild("post_id").endAt(lastKey).limitToLast(QUERY_LIMIT);
        } */
    }

    public class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    //add loading view
                    listViewComment.addFooterView(ftView);
                    break;
                case 1 :
                    //update data
                    collegeAdapter.addListItemToAdapter((ArrayList<TimelinePosts>)msg.obj);
                    listViewComment.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;

            }
        }
    }
    private ArrayList<TimelinePosts> getMoreData (){
        final ArrayList<TimelinePosts> lst = new ArrayList<>();

        if (lastKey!= null){
            lst.clear();
            orderData =  database.endAt(lastKey).limitToLast(QUERY_LIMIT *page);
        }
        else {
            orderData = database.endAt(lastKey).limitToLast(QUERY_LIMIT);
        }

        orderData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                page++;

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TimelinePosts artist = postSnapshot.getValue(TimelinePosts.class);
                    lst.add(artist);
                    lastKey = String.valueOf(artist.getPost_id());
                }

               // Collections.reverse(lst);
                /*creating adapter
                collegeAdapter = new TimelineActivity(TimelineListActivity.this, lst);
                //attaching adapter to the listview
                listViewComment.setAdapter(collegeAdapter);
                listViewComment.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);  */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        return lst;
    }
    public class ThreadGetMoreData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);

            ArrayList<TimelinePosts> lstResult = getMoreData();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message msg = mHandler.obtainMessage(1, lstResult);
            mHandler.sendMessage(msg);
        }
    }
}