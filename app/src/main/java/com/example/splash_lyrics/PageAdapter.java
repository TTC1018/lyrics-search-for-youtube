package com.example.splash_lyrics;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mData;
    private PageAdapter(@NonNull FragmentManager fm){
        super(fm);
        mData = new ArrayList<>();
        mData.add(new Fragment1());
        mData.add(new Fragment2());
    }
public CharSequence getPageTitle(int position){
        return (position+1)+"번쨰";
}
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
