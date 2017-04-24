package com.example.abhinav.buzzer.Test2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.buzzer.R;
import com.example.abhinav.buzzer.Test2.Question;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Question> mQuestionList;
    private Activity context;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEvent, textViewUser, postPost;
        CircleImageView user_pic;
        ImageView postImage;


        MyViewHolder(View view) {
            super(view);
            textViewEvent = (TextView) view.findViewById(R.id.post_event);
            textViewUser = (TextView) view.findViewById(R.id.postUsername);
            user_pic = (CircleImageView) view.findViewById(R.id.user_pic);
            postImage = (ImageView) view.findViewById(R.id.post_image);
            postPost = (TextView)view.findViewById(R.id.post_text);
        }
    }

    RecyclerViewAdapter(List<Question> mQuestionList) {
        this.mQuestionList = mQuestionList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Question question = mQuestionList.get(position);
        holder.textViewEvent.setText(question.getEvent());
        holder.postPost.setText(question.getPost());
        holder.textViewUser.setText(question.getUsername());

        Picasso.with(context).load(question.getProfile_pic()).into(holder.user_pic);
        Picasso.with(context).load(question.getImage()).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }
}