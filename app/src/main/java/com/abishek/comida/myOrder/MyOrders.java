package com.abishek.comida.myOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abishek.comida.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity {

    private ArrayList<String> titles;
    private TabLayout mTabLayout;
    private int selected_position;
    private ArrayList<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        selected_position = getIntent().getIntExtra("viewpagerId",0);
        titles = new ArrayList<>();
        titles.add("New Order");
        titles.add("Order History");


        mTabLayout = findViewById(R.id.tabs_services);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NewOrder());
        mFragmentList.add(new NewOrder());

        setViewPager();
    }


    private void setViewPager() {

        ViewPager viewPager = findViewById(R.id.viewpager_car_services);

        MyOrdersPagerAdapter adapter = new MyOrdersPagerAdapter(
                getSupportFragmentManager(), mFragmentList);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selected_position);
        //  refreshFragment(adapter, selected_position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //  refreshFragment(adapter, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setUpCustomTabs();

    }


    private void setUpCustomTabs() {

        for (int i = 0; i < titles.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.custom_tabview2, null);

            TextView tv = view.findViewById(R.id.tab_title);
            tv.setText(titles.get(i));


            TabLayout.Tab tab = mTabLayout.getTabAt(i);

            if (tab != null)
                tab.setCustomView(view);//set custom view
        }



        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}