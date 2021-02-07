package com.abishek.comida.myOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abishek.comida.R;
import com.abishek.comida.home.HomePage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity {

    private ArrayList<String> titles;
    private TabLayout tabLayout;
    private int selected_position;
    private ArrayList<Fragment> mFragmentList;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);


        setTabLayout();


    }


    public void setTabLayout() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("New Order"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));


        final MyOrdersPagerAdapter adapter = new MyOrdersPagerAdapter(MyOrders.this, getSupportFragmentManager(),
                tabLayout.getTabCount(), viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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