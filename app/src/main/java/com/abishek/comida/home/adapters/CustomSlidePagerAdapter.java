package com.abishek.comida.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;


import com.abishek.comida.R;

import java.util.ArrayList;

public class CustomSlidePagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Integer> pagerList;

    public CustomSlidePagerAdapter(Context context, ArrayList<Integer> pager) {
        this.context = context;
        this.pagerList = pager;
    }

    @Override
    public int getCount() {

        return pagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public  Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_show_layout_design, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        imageView.setBackgroundResource(pagerList.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
