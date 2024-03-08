package com.example.walletwise.UserInfoAndHomeScreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.walletwise.Earnings.EarningScreens.EarningListFragment;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendingScreens.SpendingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class AppScreen extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bnv;
    int flag=-1;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_screen);
        bnv = findViewById(R.id.bnv_appScreen);
        bnv.setOnItemSelectedListener(this);
        bnv.setSelectedItemId(R.id.action_home);
        replaceFragment(new HomeScreenFragment());




    }


    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.AppFragmentContainer,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_home){
            replaceFragment(new HomeScreenFragment());
        }
        if(item.getItemId()==R.id.action_spending_tracker){
            replaceFragment(new SpendingListFragment());
            flag=1;
        }
        if(item.getItemId()==R.id.action_wage_counter){
            replaceFragment(new EarningListFragment());
            flag=0;
        }
        return true;

    }
}

