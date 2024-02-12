package com.example.walletwise.UserInfoAndHomeScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.walletwise.Earnings.EarningScreens.EarningListFragment;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendingScreens.SpendingListFragment;
import com.example.walletwise.UserInfoAndHomeScreen.HomeScreenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class AppScreen extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bnv;


    SmoothBottomBar snb;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_screen);
        snb = findViewById(R.id.snb_spendingScreen);
        bnv = findViewById(R.id.bnv_appScreen);
        bnv.setOnItemSelectedListener(this);
        bnv.setSelectedItemId(R.id.action_home);
        snb.setItemActiveIndex(1);
        replaceFragment(new HomeScreenFragment());
        snb.setBarCorners(30);

        snb.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                if(i==0){
                    replaceFragment(new SpendingListFragment());
                }
                if(i==1){
                    replaceFragment(new HomeScreenFragment());
                }
                if(i==2){
                    replaceFragment( new EarningListFragment());
                }

                return false;
            }
        });


    }


    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.SpendingScreenFragmentContainer,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_home){
            replaceFragment(new HomeScreenFragment());
        }
        if(item.getItemId()==R.id.action_spending_tracker){
            replaceFragment(new SpendingListFragment());
        }
        if(item.getItemId()==R.id.action_wage_counter){
            replaceFragment(new EarningListFragment());
        }
        return true;

    }
}

