package com.tech.abhinav.buzzer.Event;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tech.abhinav.buzzer.R;

import java.util.List;


public class EventActivity extends ArrayAdapter<EventName> {
    private Activity context;
    List<EventName> cname;

    public EventActivity(Activity context, List<EventName> artists) {
        super(context, R.layout.event_list, artists);
        this.context = context;
        this.cname = artists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.event_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView eventDate = (TextView) listViewItem.findViewById(R.id.eventDate);


        EventName name = cname.get(position);
        textViewName.setText(name.getEventName());
        eventDate.setText(name.getEventDate());

        return listViewItem;
    }


}
