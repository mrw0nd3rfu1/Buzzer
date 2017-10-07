package com.tech.abhinav.buzzer.Test;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tech.abhinav.buzzer.R;
import com.tech.abhinav.buzzer.Timeline.MainActivity;
import com.tech.abhinav.buzzer.Utility.Home;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends AppCompatActivity {

    private String mChatUser;
    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;
    private String mCurrentUserId;

    private RecyclerView mMessagesList;
    private final List<Home> messageList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapterTest mAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;

    //extra
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey="";

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mCollege;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth mAuth;
    private Query orderData;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessLike = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mAdapter = new MessageAdapterTest(messageList);

        mMessagesList = (RecyclerView) findViewById(R.id.home_list);
      //  mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);
        //mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.setAdapter(mAdapter);
        
        loadMessages();


        //new

        //mDatabase = FirebaseDatabase.getInstance().getReference().child(MainActivity.clgID).child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        //orderData = mDatabase.orderByChild("post_id").limitToFirst(30);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);
        orderData.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();



        //refresh button
        mMessagesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                mCurrentPage++;
                itemPos=0;
                loadMoreMessages();
            }




        });
    }

    private void loadMoreMessages() {
        mDatabase = mRootRef.child(MainActivity.clgID).child("Post");

        orderData = mDatabase.orderByKey().endAt(mLastKey).limitToLast(10);

        orderData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Home post = dataSnapshot.getValue(Home.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)){
                    messageList.add(itemPos++ ,post);

                }
                else {
                    mPrevKey = mLastKey;
                }

                if (itemPos==1){
                    mLastKey = messageKey;
                }


                Log.d("TotalKey","LastKey " + mLastKey + "PrevKey" + mPrevKey + "MessageKey" + messageKey);

                mAdapter.notifyDataSetChanged();
                //mRefreshLayout.setRefreshing(false);
                mLinearLayout.scrollToPositionWithOffset(10,0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadMessages() {

        mDatabase = mRootRef.child(MainActivity.clgID).child("Post");

        orderData = mDatabase.orderByKey().endAt(mLastKey).limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

           orderData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Home message = dataSnapshot.getValue(Home.class);
                itemPos++;
                if (itemPos==1){
                     String messageKey = dataSnapshot.getKey();
                    mLastKey = messageKey;
                    mPrevKey = messageKey;
                }

                messageList.add(message);
                mAdapter.notifyDataSetChanged();

               // mMessagesList.scrollToPosition(messageList.size() - 1);

                //mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
