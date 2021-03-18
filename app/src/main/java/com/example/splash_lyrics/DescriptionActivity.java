package com.example.splash_lyrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_description);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(8);

        PageAdapter adapter =  new PageAdapter(getSupportFragmentManager());
        Fragment1 f1 = new Fragment1();
        Fragment2 f2 = new Fragment2();
        Fragment3 f3 = new Fragment3();
        Fragment4 f4 = new Fragment4();
        Fragment5 f5 = new Fragment5();
        Fragment6 f6 = new Fragment6();
        Fragment7 f7 = new Fragment7();
        Fragment8 f8 = new Fragment8();



        adapter.addItem(f1);
        adapter.addItem(f2);
        adapter.addItem(f3);
        adapter.addItem(f4);
        adapter.addItem(f5);
        adapter.addItem(f6);
        adapter.addItem(f7);
        adapter.addItem(f8);





        viewPager.setAdapter(adapter);

    }
    class PageAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mData = new ArrayList<Fragment>();
        public PageAdapter(@NonNull FragmentManager fm) {
            super(fm);
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
        public void addItem(Fragment Item){
            mData.add(Item);
        }
    }



}