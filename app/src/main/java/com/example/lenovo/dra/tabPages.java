package com.example.lenovo.dra;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.lenovo.dra.fragments.homeFragment;
import com.example.lenovo.dra.fragments.profileFragment;
import com.example.lenovo.dra.fragments.readFragment;

public class tabPages extends FragmentStatePagerAdapter {

    String[] titles = new String[]{"HOME", "READ", "PROFILE"};

    public tabPages(FragmentManager fm){
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                homeFragment homeFragment = new homeFragment();
                return homeFragment;

            case 1:
                readFragment readFragment = new readFragment();
                return readFragment;

            case 2:
                profileFragment profileFragment = new profileFragment();
                return profileFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
