package com.tech.abhinav.buzzer.Chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Abhinav on 21-Jun-17.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1 :
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle (int position){
        return null;
    }
}
