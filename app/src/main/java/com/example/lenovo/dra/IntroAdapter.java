package com.example.lenovo.dra;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(Color.parseColor("#b18a7d"), position);
            case 1:
                return IntroFragment.newInstance(Color.parseColor("#b18a7d"), position);
            default:
                return IntroFragment.newInstance(Color.parseColor("#b18a7d"), position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
