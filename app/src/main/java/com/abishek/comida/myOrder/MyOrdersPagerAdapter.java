package com.abishek.comida.myOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyOrdersPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    public MyOrdersPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "New Order";
            case 1:
                return "Order History";


        }

        return super.getPageTitle(position);
    }
}
