package com.tech.abhinav.buzzer.Profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tech.abhinav.buzzer.FragmentsProfile.PostsFragment;
import com.tech.abhinav.buzzer.FragmentsProfile.ProfileFragment;

/**
 * Created by Abhinav on 21-Jun-17.
 */

class SectionsPagerAdapter2 extends FragmentPagerAdapter {
    public SectionsPagerAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 1 :
                PostsFragment postsFragment = new PostsFragment();
                return postsFragment;
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
            case 0 : return "PROFILE";
            case 1 : return  "POSTS";
            default: return  null;
        }
    }
}
