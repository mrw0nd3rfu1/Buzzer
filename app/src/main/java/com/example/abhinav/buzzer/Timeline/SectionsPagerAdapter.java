package com.example.abhinav.buzzer.Timeline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.abhinav.buzzer.Fragments.EventFragment;
import com.example.abhinav.buzzer.Fragments.HomeFragment;
import com.example.abhinav.buzzer.Fragments.LikedFragment;

/**
 * Created by Abhinav on 21-Jun-17.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                HomeFragment requestsFragment = new HomeFragment();
                return requestsFragment;
            case 1 :
                EventFragment chatsFragment = new EventFragment();
                return chatsFragment;
            case 2 :
                LikedFragment friendsFragment = new LikedFragment();
                return friendsFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle (int position){
        switch (position){
            case 0 : return "HOME";
            case 1 : return  "EVENTS";
            case 2 : return  "LIKED";
            default: return  null;
        }
    }
}
