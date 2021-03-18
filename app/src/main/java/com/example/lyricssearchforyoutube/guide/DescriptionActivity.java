package com.example.lyricssearchforyoutube.guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;

import com.example.lyricssearchforyoutube.R;

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
        GuideFragment1 f1 = new GuideFragment1();
        GuideFragment2 f2 = new GuideFragment2();
        GuideFragment3 f3 = new GuideFragment3();
        GuideFragment4 f4 = new GuideFragment4();
        GuideFragment5 f5 = new GuideFragment5();
        GuideFragment6 f6 = new GuideFragment6();
        GuideFragment7 f7 = new GuideFragment7();
        GuideFragment8 f8 = new GuideFragment8();

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