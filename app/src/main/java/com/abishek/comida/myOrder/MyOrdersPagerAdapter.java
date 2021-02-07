package com.abishek.comida.myOrder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class MyOrdersPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    ViewPager viewPager;
    public MyOrdersPagerAdapter(Context c, FragmentManager fm, int totalTabs, ViewPager viewPager) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = c;
        this.totalTabs = totalTabs;
        this.viewPager = viewPager;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewOrder();
            case 1:
                return new OrderHistory();

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
