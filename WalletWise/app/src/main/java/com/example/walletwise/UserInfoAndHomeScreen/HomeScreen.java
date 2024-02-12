package com.example.walletwise.UserInfoAndHomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.Earnings.EarningScreens.EarningScreen;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.Spendings.SpendingScreens.SpendingScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    TextView tvUserName, tvLogOut,tvCurDate,tvCurCashState,tvCurCashStateNum,tvCurMonthEarningsNum,tvCurMonthSpendingsNum,tvState;

    String Months[];
    Calendar c = Calendar.getInstance();
    int curMonth = c.get(Calendar.MONTH),curYear= c.get(Calendar.YEAR);
    String name="",curMonthDisplay="",DateToDisplay = "",curYearS=Integer.toString(curYear),curMonthS="",spendSumS="",earnSumS="",sumstateS="";
    BottomNavigationView bnv;
    FloatingActionButton fabNextMonth1,fabPrevMonth1;
    SpendingsOpenHelper soh;
    EarningsOpenHelper eoh;

    double sumSpend=0,sumEarn=0,sumState=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        soh = new SpendingsOpenHelper(this);
        eoh = new EarningsOpenHelper(this);
        tvCurDate=findViewById(R.id.tvCurDate);
        tvUserName = findViewById(R.id.tvUserName);
        tvLogOut = findViewById(R.id.tvLogOut);
        tvCurCashState = findViewById(R.id.tvCurCashState);
        tvCurCashStateNum = findViewById(R.id.tvCurCashStateNum);
        tvCurMonthEarningsNum = findViewById(R.id.tvCurMonthEarningsNum);
        tvCurMonthSpendingsNum = findViewById(R.id.tvCurMonthSpendingsNum);
        tvState = findViewById(R.id.tvState);
        fabNextMonth1 = findViewById(R.id.fabNextMonth1);
        fabPrevMonth1 = findViewById(R.id.fabPrevMonth1);
        SharedPreferences getName = getSharedPreferences("setUp", Context.MODE_PRIVATE);
        name=getName.getString("name","");
        tvUserName.setText(" " +"ברוך הבא "+name);
        tvLogOut.setOnClickListener(this);
        fabNextMonth1.setOnClickListener(this);
        fabPrevMonth1.setOnClickListener(this);
        bnv = findViewById(R.id.bottom_navigation_home);
        bnv.setSelectedItemId(R.id.action_home);
        bnv.setOnItemSelectedListener(this);
        Months=getResources().getStringArray(R.array.months);
        curMonthDisplay = Months[curMonth];
        DateToDisplay = curMonthDisplay + " " + curYearS;
        tvCurDate.setText(DateToDisplay);

        curMonthS=Integer.toString(curMonth+1);
        sumSpend=soh.getSumSpendingByDate(curMonthS,curYearS);
        sumEarn=eoh.getSumEarningByDate(curMonthS,curYearS);
        spendSumS=Double.toString(sumSpend);
        earnSumS=Double.toString(sumEarn);
        sumState=sumEarn-sumSpend;
        sumstateS=sumState+"₪";
        tvCurMonthSpendingsNum.setText(spendSumS+"₪");
        tvCurMonthEarningsNum.setText(earnSumS+"₪");
        tvCurCashStateNum.setText(sumstateS);
        bnv.setItemIconTintList(null);
        if(sumState>0){
            tvState.setTextColor(getColor(R.color.green));
            tvState.setText("ברווח ");
            tvCurCashStateNum.setTextColor(getColor(R.color.green));
        }
        if(sumState<0){
            tvState.setTextColor(getColor(R.color.red));
            tvState.setText("בהפסד ");
            tvCurCashStateNum.setTextColor(getColor(R.color.red));

        }
        if(sumState==0) {
            tvState.setText("במצב ניטרלי ");
            tvState.setTextColor(getColor(R.color.white));
            tvCurCashStateNum.setTextColor(getColor(R.color.white));

        }
    }
    


    @Override
    public void onClick(View v) {
        if (v == tvLogOut) {
            Intent i = new Intent(this, SetUpActivity.class);
            startActivity(i);

        }
        if(v==fabNextMonth1){
            curMonth++;
            if(curMonth==12){
                curMonth=0;
                curYear++;
            }
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurDate.setText(DateToDisplay);
            curMonthS=Integer.toString(curMonth+1);
            sumSpend=soh.getSumSpendingByDate(curMonthS,curYearS);
            sumEarn=eoh.getSumEarningByDate(curMonthS,curYearS);
            earnSumS=Double.toString(sumEarn);
            spendSumS=Double.toString(sumSpend);
            sumState=sumEarn-sumSpend;
            sumstateS="₪"+ sumState;
            tvCurMonthSpendingsNum.setText(spendSumS+"₪");
            tvCurMonthEarningsNum.setText(earnSumS+"₪");
            tvCurCashStateNum.setText(sumstateS);
            if(sumState>0){
                tvState.setTextColor(getColor(R.color.green));
                tvState.setText("ברווח של: ");
            }
            if(sumState<0){
                tvState.setTextColor(getColor(R.color.red));
                tvState.setText("בהפסד של: ");
            }
            else {
                tvState.setText("במצב ניטרלי: ");
                tvState.setTextColor(getColor(R.color.white));
            }
        }
        if(v==fabPrevMonth1){
            curMonth--;
            if(curMonth==-1){
                curMonth=11;
                curYear--;
            }
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurDate.setText(DateToDisplay);
            curMonthS=Integer.toString(curMonth+1);
            sumSpend=soh.getSumSpendingByDate(curMonthS,curYearS);
            sumEarn=eoh.getSumEarningByDate(curMonthS,curYearS);
            earnSumS=Double.toString(sumEarn);
            spendSumS=Double.toString(sumSpend);
            sumState=sumEarn-sumSpend;
            sumstateS="₪"+ sumState;
            tvCurMonthSpendingsNum.setText(spendSumS+"₪");
            tvCurMonthEarningsNum.setText(earnSumS+"₪");
            tvCurCashStateNum.setText(sumstateS);
            if(sumState>0){
                tvState.setTextColor(getColor(R.color.green));
                tvState.setText("ברווח של: ");
            }
            else if(sumState<0){
                tvState.setTextColor(getColor(R.color.red));
                tvState.setText("בהפסד של: ");
            }
            else {
                tvState.setText("במצב ניטרלי: ");
                tvState.setTextColor(getColor(R.color.white));
            }

        }

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            return true;
        } else if (item.getItemId() == R.id.action_spending_tracker) {
            startActivity(new Intent(this, SpendingScreen.class));
            return true;

        } else if (item.getItemId() == R.id.action_wage_counter) {
            startActivity(new Intent(this, EarningScreen.class)); // Change to the appropriate activity
            return true;
        }
        else {
            return false;
        }
    }
}









