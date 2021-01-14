package com.abishek.comida.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.abishek.comida.R;
import com.abishek.comida.home.fragments.Home;
import com.abishek.comida.home.fragments.More;
import com.abishek.comida.home.fragments.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private ImageView navProfile, navMore, cartIcon;
    private FloatingActionButton navHome;
    private int bottomOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        inItUi();
        loadFragment(new Home());
        bottomOption = 2;
    }

    public void inItUi() {
        navProfile = findViewById(R.id.profile);
        navHome = findViewById(R.id.home);
        navMore = findViewById(R.id.more);
        cartIcon = findViewById(R.id.cart);

        navMore.setOnClickListener(this);
        navHome.setOnClickListener(this);
        navProfile.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home:
                if (bottomOption != 2) {
                    setBottomIcon(navHome);
                    bottomOption = 2;
                    loadFragment(new Home());
                }
                break;
            case R.id.more:
                if (bottomOption != 3) {
                    setBottomIcon(navMore);
                    loadFragment(new More());
                    bottomOption = 3;
                }
                break;
            case R.id.profile:
                if (bottomOption != 1) {
                    setBottomIcon(navProfile);
                    loadFragment(new Profile());
                    bottomOption = 1;
                }
                break;
            case R.id.cart:
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragment_container, fragment);
        // transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setBottomIcon(ImageView imageView) {
        navHome.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HomePage.this, R.color.icon_unselect)));
        //  navHome.setBackgroundColor(ContextCompat.getColor(HomePage.this,R.color.icon_unselect));
        //  navHome.setColorFilter(ContextCompat.getColor(this, R.color.icon_unselect), android.graphics.PorterDuff.Mode.SRC_IN);
        navProfile.setColorFilter(ContextCompat.getColor(this, R.color.icon_unselect), android.graphics.PorterDuff.Mode.SRC_IN);
        navMore.setColorFilter(ContextCompat.getColor(this, R.color.icon_unselect), android.graphics.PorterDuff.Mode.SRC_IN);

        imageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void setBottomIcon(FloatingActionButton fab) {
        /// navHome.setColorFilter(ContextCompat.getColor(this, R.color.icon_unselect), android.graphics.PorterDuff.Mode.SRC_IN);
        navProfile.setColorFilter(ContextCompat.getColor(this, R.color.icon_unselect), android.graphics.PorterDuff.Mode.SRC_IN);
        navMore.setColorFilter(ContextCompat.getColor(this, R.color.icon_unselect), android.graphics.PorterDuff.Mode.SRC_IN);

        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HomePage.this, R.color.colorPrimary)));
    }


    @Override
    public void onBackPressed() {


        if (bottomOption == 2)
            super.onBackPressed();
        else {
            setBottomIcon(navHome);
            bottomOption = 2;
            loadFragment(new Home());
        }
    }
}