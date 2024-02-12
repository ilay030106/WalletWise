package com.example.walletwise.Earnings.EarningScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.walletwise.Earnings.EarnOpenHelper.Earning;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningAdapter;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningAdd;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendingScreens.SpendingScreen;
import com.example.walletwise.UserInfoAndHomeScreen.HomeScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class EarningScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {
    TextView tvCurEarningMonth,tvMonthlyHours,tvMonthlyEarnings;
    BottomNavigationView bnv;
    String curYearS = "",curMonthDisplay="",DateToDisplay = "",curMonthS="",sumEarn="",monthlyHours="",sumPay="",sumTips="";
    String Months[];
    Calendar c = Calendar.getInstance();
    int curMonth = c.get(Calendar.MONTH),curYear= c.get(Calendar.YEAR),hours=0,minutes=0;
    FloatingActionButton fabNextMonth,fabPrevMonth,fabInfo,fabAddEarningShift;

    EarningsOpenHelper eoh;
    ListView listView;
    ArrayList<Earning> listOfEarnings;
    EarningAdapter earningAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_screen);
        listView = findViewById(R.id.listviewE);
        eoh= new EarningsOpenHelper(this);
        listOfEarnings = new ArrayList<Earning>();
        eoh.open();
        listOfEarnings = eoh.searchEarnings(Integer.toString(curMonth+1), curYearS, "", "", -1,-1,-1,-1);//לבדוק למה בהתחלה הוא לא מציג ישר את הבזבוזים של אותו חודש
        eoh.close();
        earningAdapter = new EarningAdapter(this, 0, listOfEarnings);
        listView.setAdapter(earningAdapter);
        tvCurEarningMonth = findViewById(R.id.tvCurEarningMonth);
        fabNextMonth = findViewById(R.id.fabNextMonth);
        fabPrevMonth = findViewById(R.id.fabPrevMonth);
        fabInfo = findViewById(R.id.fabInfo);
        fabAddEarningShift = findViewById(R.id.fabAddEarningShift);
        bnv = findViewById(R.id.bottom_navigation_earning);
        tvMonthlyHours=findViewById(R.id.tvMonthlyHours);
        tvMonthlyEarnings=findViewById(R.id.tvMonthlyEarnings);
        bnv.setSelectedItemId(R.id.action_wage_counter);
        Months=getResources().getStringArray(R.array.months);
        curYearS = Integer.toString(curYear);
        curMonthDisplay = Months[curMonth];
        DateToDisplay = curMonthDisplay + " " + curYearS;
        tvCurEarningMonth.setText(DateToDisplay);
        fabPrevMonth.setOnClickListener(this);
        fabNextMonth.setOnClickListener(this);
        fabInfo.setOnClickListener(this);
        fabAddEarningShift.setOnClickListener(this);
        bnv.setItemIconTintList(null);
        bnv.setOnItemSelectedListener(this);
        listView.setOnItemClickListener(this);

        curMonthS=Integer.toString(curMonth+1);
        sumEarn= "₪" + eoh.getSumEarningByDate(curMonthS, curYearS);
        sumPay= "₪" + eoh.getSumPaymentByDate(curMonthS, curYearS);
        sumTips= "₪" + eoh.getSumTipsByDate(curMonthS, curYearS);
        hours= eoh.calculateHoursbyDate(curMonthS,curYearS);
        minutes= eoh.calculateMinutesbyDate(curMonthS,curYearS);
        monthlyHours = String.format("%02d:%02d", hours, minutes);
        tvMonthlyEarnings.setText(" "+sumEarn);
        tvMonthlyHours.setText(monthlyHours+"שעות");

        SharedPreferences earnInfo=getSharedPreferences("earnInfo",MODE_PRIVATE);
        SharedPreferences.Editor earnInfoEdit=earnInfo.edit();
        earnInfoEdit.putInt("month",curMonth+1);
        earnInfoEdit.putInt("year",curYear);
        earnInfoEdit.commit();



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            startActivity(new Intent(this, HomeScreen.class)); // Change to the appropriate activity
            return true;
        } else if (item.getItemId() == R.id.action_spending_tracker) {
            startActivity(new Intent(this, SpendingScreen.class));
            return true;

        } else if (item.getItemId() == R.id.action_wage_counter) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if(v==fabNextMonth){
            curMonth++;
            if(curMonth==12){
                curMonth=0;
                curYear++;
            }
            curMonthS=Integer.toString(curMonth+1);
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurEarningMonth.setText(DateToDisplay);
            sumEarn= "₪" + eoh.getSumEarningByDate(curMonthS, curYearS);
            sumPay= "₪" + eoh.getSumPaymentByDate(curMonthS, curYearS);
            sumTips= "₪" + eoh.getSumTipsByDate(curMonthS, curYearS);
            hours= eoh.calculateHoursbyDate(curMonthS,curYearS);
            minutes= eoh.calculateMinutesbyDate(curMonthS,curYearS);
            monthlyHours = String.format("%02d:%02d", hours, minutes);
            tvMonthlyEarnings.setText(" "+sumEarn);
            tvMonthlyHours.setText(monthlyHours+"שעות");
            SharedPreferences earnInfo=getSharedPreferences("earnInfo",MODE_PRIVATE);
            SharedPreferences.Editor earnInfoEdit=earnInfo.edit();
            earnInfoEdit.putInt("month",curMonth+1);
            earnInfoEdit.putInt("year",curYear);
            earnInfoEdit.commit();


        }
        if(v==fabPrevMonth){
            curMonth--;
            if(curMonth==-1){
                curMonth=11;
                curYear--;
            }
            curMonthS=Integer.toString(curMonth+1);
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurEarningMonth.setText(DateToDisplay);
            sumEarn= "₪" + eoh.getSumEarningByDate(curMonthS, curYearS);
            sumPay= "₪" + eoh.getSumPaymentByDate(curMonthS, curYearS);
            sumTips= "₪" + eoh.getSumTipsByDate(curMonthS, curYearS);
            hours= eoh.calculateHoursbyDate(curMonthS,curYearS);
            minutes= eoh.calculateMinutesbyDate(curMonthS,curYearS);
            monthlyHours = String.format("%02d:%02d", hours, minutes);
            tvMonthlyEarnings.setText(" "+sumEarn);
            tvMonthlyHours.setText(monthlyHours+"שעות");
            SharedPreferences earnInfo=getSharedPreferences("earnInfo",MODE_PRIVATE);
            SharedPreferences.Editor earnInfoEdit=earnInfo.edit();
            earnInfoEdit.putInt("month",curMonth+1);
            earnInfoEdit.putInt("year",curYear);
            earnInfoEdit.commit();


        }
        if(v==fabAddEarningShift){
            startActivity(new Intent(this, EarningAdd.class));
        }
        if(v==fabInfo){
            EarningDesc earningDesc = new EarningDesc();
            earningDesc.show(getSupportFragmentManager(), "EarningDesc");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Earning earning = earningAdapter.getItem(position);
        Intent i = new Intent(EarningScreen.this, UpdateEarning.class);
        i.putExtra("rowId", earning.getId());
        startActivity(i);
    }
}
