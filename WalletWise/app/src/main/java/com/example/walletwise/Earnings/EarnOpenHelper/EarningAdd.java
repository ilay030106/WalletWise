package com.example.walletwise.Earnings.EarnOpenHelper;

import static com.example.walletwise.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walletwise.Earnings.EarnOpenHelper.Earning;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.Earnings.EarningScreens.EarningScreen;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendingScreens.SpendingScreen;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class EarningAdd extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    TextView tvStartDate,tvStartTime,tvEndDate,tvEndTime;
    Button btnStartDate,btnEndDate,btnStartTime,btnEndTime,btnAddShift;
    CheckBox cbTip;
    TextInputEditText etTip;
    TextInputLayout layTip;
    double money=0,tip=0,wage=0;

    FloatingActionButton fabCloseEarn;
    String startDate="",endDate="",startTime="",endTime="",wageS="",tipS="";
    MaterialTimePicker startTimePicker,endTimePicker;//M3 Google library
    MaterialDatePicker startDatePicker,endDatePicker;//M3 Google library
    EarningsOpenHelper eoh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_earning_add);
        SharedPreferences getWage = getSharedPreferences("setUp", Context.MODE_PRIVATE);
        wageS=getWage.getString("wage"," ");
        wage=Double.parseDouble(wageS);
        tvStartDate=findViewById(R.id.tvStartDate);
        tvStartTime=findViewById(R.id.tvStartTime);
        tvEndDate=findViewById(R.id.tvEndDate);
        tvEndTime=findViewById(id.tvEndTime);
        btnStartDate=findViewById(R.id.btnStartDate);
        btnStartDate.setOnClickListener(this);
        btnEndDate=findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(this);
        btnStartTime=findViewById(R.id.btnStartTime);
        btnStartTime.setOnClickListener(this);
        btnEndTime=findViewById(R.id.btnEndTime);
        btnEndTime.setOnClickListener(this);
        btnAddShift=findViewById(R.id.btnAddShift);
        btnAddShift.setOnClickListener(this);
        fabCloseEarn=findViewById(id.fabCloseEarn);
        fabCloseEarn.setOnClickListener(this);
        cbTip=findViewById(id.cbTip);
        cbTip.setOnCheckedChangeListener(this);
        etTip=findViewById(id.etTip);
        layTip=findViewById(id.layTip);
        etTip.setHintTextColor(getResources().getColor(R.color.lightGrey));
        cbTip.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
        eoh= new EarningsOpenHelper(this);
        startTimePicker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("בחר את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();
        endTimePicker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("בחר את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();
        startDatePicker= MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("בחר את תאריך תחילת המשמרת")
                .build();
        endDatePicker= MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("בחר את תאריך סוף המשמרת")
                .build();
        startDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar systemCalender = Calendar.getInstance();
                systemCalender.setTimeInMillis(selection);
                int year = systemCalender.get(Calendar.YEAR);
                int monthOfYear = systemCalender.get(Calendar.MONTH)+1;
                int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
                tvStartDate.setVisibility(View.VISIBLE);
                startDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear,year);
                btnStartDate.setText(startDate);
                startDatePicker.dismiss();
            }
        });
        endDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar systemCalender = Calendar.getInstance();
                systemCalender.setTimeInMillis(selection);
                int year = systemCalender.get(Calendar.YEAR);
                int monthOfYear = systemCalender.get(Calendar.MONTH)+1;
                int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
                tvEndDate.setVisibility(View.VISIBLE);
                endDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear,year);
                btnEndDate.setText(endDate);
                endDatePicker.dismiss();
            }
        });
        startTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minute=startTimePicker.getMinute();
                int hour=startTimePicker.getHour();
                startTime = String.format("%02d:%02d", hour, minute);
                tvStartTime.setVisibility(View.VISIBLE);
                btnStartTime.setText(startTime);
                startTimePicker.dismiss();
            }
        });
        endTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minute=endTimePicker.getMinute();
                int hour=endTimePicker.getHour();
                endTime = String.format("%02d:%02d", hour, minute);
                tvEndTime.setVisibility(View.VISIBLE);
                btnEndTime.setText(endTime);
                endTimePicker.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
    if (v==btnStartTime){
        startTimePicker.show(this.getSupportFragmentManager(),"StartTimePicker");
    }
    if (v==btnEndTime){
        endTimePicker.show(this.getSupportFragmentManager(),"EndTimePicker");

    }
    if (v==btnStartDate){
        startDatePicker.show(this.getSupportFragmentManager(),"StartDatePicker");

    }
    if (v==btnEndDate){
        endDatePicker.show(this.getSupportFragmentManager(),"EndDatePicker");//פותח גם דייט וגם טיים והטיים נשמר רק בפעם השנייה שמפעילים את הפיקרים
    }
    if (v==btnAddShift){
        eoh.open();
        tip=0;
        tipS=etTip.getText().toString();
        Earning earn = new Earning(startDate,startTime,endDate,endTime,money,tip);
        money=earn.calculateMinutes()*(wage/60);
        //לעשות תקינות קלט לתאריכים
        if(tipS.equals("")){
            tip=0;
        }
        else{
            tip=Double.parseDouble(tipS);
        }
        earn.setTip(tip);
        earn.setMoney(money);
        eoh.createEarning(earn);
        eoh.close();
        Toast.makeText(this,"המשמרת נשמרה בהצלחה! ",Toast.LENGTH_LONG).show();
        Intent i= new Intent(this, EarningScreen.class);
        startActivity(i);
    }
    if(v==fabCloseEarn){
        Intent i= new Intent(this, EarningScreen.class);
        startActivity(i);
    }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==cbTip&&isChecked==true){
            layTip.setVisibility(View.VISIBLE);
        }
        if(buttonView==cbTip&&isChecked==false){
            layTip.setVisibility(View.GONE);
        }

    }


}