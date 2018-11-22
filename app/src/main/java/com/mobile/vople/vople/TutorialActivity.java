package com.mobile.vople.vople;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class TutorialActivity extends FragmentActivity {
    public static TutorialActivity mInstance;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    DisplayMetrics minSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInstance = TutorialActivity.this;
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tutorial_main);

        minSize = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(minSize);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // 해당하는 page의 Fragment를 생성합니다.
            return PageFragment.create(position,minSize,getApplicationContext());
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}