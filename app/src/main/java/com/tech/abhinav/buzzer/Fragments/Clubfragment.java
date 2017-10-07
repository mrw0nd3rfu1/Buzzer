package com.tech.abhinav.buzzer.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tech.abhinav.buzzer.Clubs.ClubDetails;
import com.tech.abhinav.buzzer.R;
import com.tech.abhinav.buzzer.Timeline.MainActivity;
import com.tech.abhinav.buzzer.Clubs.ClubData;


public class Clubfragment extends Fragment {

    FirebaseRecyclerAdapter<ClubData,ClubHolder> firebaseRecyclerAdapter;
    public View mMainView;
    private int HEADER=0;
    private int ITEM_VIEW=1;


    public Clubfragment() {
        // Required empty public constructor
    }


    public static Clubfragment newInstance() {

        return new Clubfragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       mMainView= inflater.inflate(R.layout.fragment_clubfragment, container, false);
        final Activity activity=getActivity();
        RecyclerView recyclerView=(RecyclerView)mMainView.findViewById(R.id.clubrecycler);


        DatabaseReference dRef= FirebaseDatabase.getInstance().getReference().child(MainActivity.clgID).child("Clubs");
        dRef.keepSynced(true);


        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ClubData, ClubHolder>(
                ClubData.class,R.layout.card_clublist,ClubHolder.class,dRef
        )

        {


            @Override
            protected void populateViewHolder(final ClubHolder viewHolder, ClubData model, int position) {

                if(position !=0){
                    viewHolder.setImage(getContext(),model.clubImage);
                    viewHolder.setName(model.clubName);}

            }

            @Override
            public void onBindViewHolder(ClubHolder viewHolder, int position) {
                if(position!=0)
                {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View clubImage=view.findViewById(R.id.clubImage);
                            View clubName=view.findViewById(R.id.clubName);
                            Intent intent=new Intent(getContext(), ClubDetails.class);
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            {
                                Pair<View, String> pair1=Pair.create(clubImage,clubImage.getTransitionName());
                                Pair<View, String> pair2=Pair.create(clubName,clubName.getTransitionName());
                                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pair1);
                                startActivity(intent,optionsCompat.toBundle());
                            }
                            else
                            {
                                startActivity(intent);
                            }
                        }
                    });
                    super.onBindViewHolder(viewHolder,position);
                }
                else
                {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View clubImage=view.findViewById(R.id.clubImage);
                            View clubName=view.findViewById(R.id.clubName);
                            Intent intent=new Intent(getContext(), ClubDetails.class);
                            intent.putExtra("Type","Create");
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            {
                                Pair<View, String> pair1=Pair.create(clubImage,clubImage.getTransitionName());
                                Pair<View, String> pair2=Pair.create(clubName,clubName.getTransitionName());

                                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pair1);
                                startActivity(intent,optionsCompat.toBundle());

                            }
                            else
                            {
                                startActivity(intent);
                            }
                        }
                    });
                }

            }

            @Override
            public ClubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if(viewType==HEADER)
                {
                    View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_club_layout,parent,false);
                    return  new ClubHolder(v);

                }
                else if(viewType ==ITEM_VIEW)
                {
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_clublist,parent,false);
                    return  new ClubHolder(v);

                }
                return null;
            }

            @Override
            public long getItemId(int position) {
                if (position==0)
                {
                    return HEADER;
                }
                else
                {
                    return ITEM_VIEW;
                }            }



            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        return mMainView;
    }
    private class ClubHolder extends RecyclerView.ViewHolder
    {

        public ClubHolder(View itemView) {
            super(itemView);
        }
        public void setName(String clubName)
        {
            TextView textView=(TextView)itemView.findViewById(R.id.clubName);
            textView.setText(clubName);
        }
        public void setImage(Context ctx, String image)
        {
            ImageView imageView=(ImageView)itemView.findViewById(R.id.clubImage);
            Picasso.with(ctx).load(image).into(imageView);

        }
    }

}
