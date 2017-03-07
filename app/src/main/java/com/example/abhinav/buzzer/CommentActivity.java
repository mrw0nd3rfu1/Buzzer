package com.example.abhinav.buzzer;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class CommentActivity extends ArrayAdapter<CommentPosts> {
    private Activity context;
    List<CommentPosts> cname;

    public CommentActivity(Activity context, List<CommentPosts> artists) {
        super(context , R.layout.comment_layout, artists);
        this.context = context;
        this.cname = artists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.comment_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewUser = (TextView) listViewItem.findViewById(R.id.textViewUser);

        CommentPosts name = cname.get(position);
        textViewName.setText(name.getuserName());
        textViewUser.setText(name.getComment());


        return listViewItem;
    }


}
