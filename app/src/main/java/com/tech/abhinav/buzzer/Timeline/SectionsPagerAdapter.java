package com.tech.abhinav.buzzer.Timeline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tech.abhinav.buzzer.Fragments.EventFragment;
import com.tech.abhinav.buzzer.Fragments.HomeFragment;

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
           default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle (int position){
        switch (position){
            case 0 : return "HOME";
            case 1 : return  "EVENTS";
           default: return  null;
        }
    }
}
