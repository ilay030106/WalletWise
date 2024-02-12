package com.example.walletwise.Earnings.EarningScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walletwise.Earnings.EarnOpenHelper.Earning;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendingScreens.SpendingScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class UpdateEarning extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    long id=0;
    TextView tvStartDateU,tvStartTimeU,tvEndDateU,tvEndTimeU;
    MaterialButton btnStartDateU,btnEndDateU,btnStartTimeU,btnEndTimeU,btnUpdateShift,btnDeleteShift;
    CheckBox cbTipU;
    TextInputEditText etTipU;
    TextInputLayout layTipU;
    double money=0,tip=0,wage=0;

    FloatingActionButton fabCloseEarn;
    String startDate="",endDate="",startTime="",endTime="",wageS="",tipS="";
    MaterialTimePicker startTimePickerU,endTimePickerU;//M3 Google library
    MaterialDatePicker startDatePickerU,endDatePickerU;//M3 Google library
    EarningsOpenHelper eoh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_earning);
        eoh= new EarningsOpenHelper(this);
        SharedPreferences getWage = getSharedPreferences("setUp", Context.MODE_PRIVATE);
        wageS=getWage.getString("wage"," ");
        wage=Double.parseDouble(wageS);
        tvStartDateU=findViewById(R.id.tvStartDateU);
        tvStartTimeU=findViewById(R.id.tvStartDateU);
        tvEndDateU=findViewById(R.id.tvStartDateU);
        tvEndTimeU=findViewById(R.id.tvStartDateU);
        btnStartDateU=findViewById(R.id.btnStartDateU);
        btnStartDateU.setOnClickListener(this);
        btnEndDateU=findViewById(R.id.btnEndDateU);
        btnEndDateU.setOnClickListener(this);
        btnStartTimeU=findViewById(R.id.btnStartTimeU);
        btnStartTimeU.setOnClickListener(this);
        btnEndTimeU=findViewById(R.id.btnEndTimeU);
        btnEndTimeU.setOnClickListener(this);
        btnUpdateShift=findViewById(R.id.btnUpdateShift);
        btnUpdateShift.setOnClickListener(this);
        btnDeleteShift=findViewById(R.id.btnDeleteShift);
        btnDeleteShift.setOnClickListener(this);
        fabCloseEarn=findViewById(R.id.fabCloseEarnU);
        fabCloseEarn.setOnClickListener(this);
        cbTipU=findViewById(R.id.cbTipU);
        cbTipU.setOnCheckedChangeListener(this);
        etTipU=findViewById(R.id.etTipU);
        layTipU=findViewById(R.id.layTipU);
        etTipU.setHintTextColor(getResources().getColor(R.color.lightGrey));
        cbTipU.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
        id = getIntent().getLongExtra("rowId", 0);
        if (id != 0) {
            eoh.open();
            Earning c = eoh.getEarningByID(id);
            eoh.close();
            btnStartDateU.setText(c.getStartDate());
            btnStartTimeU.setText(c.getStartTime());
            btnEndDateU.setText(c.getEndDate());
            btnEndTimeU.setText(c.getEndTime());
            if(c.getTip()>0){
                cbTipU.setChecked(true);
                layTipU.setVisibility(View.VISIBLE);
                etTipU.setText(Double.toString(c.getTip()));
            }

        }
        startTimePickerU=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("עדכן את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();
        endTimePickerU=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("עדכן את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();
        startDatePickerU= MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("עדכן את תאריך תחילת המשמרת")
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();
        endDatePickerU= MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("עדכן את תאריך סוף המשמרת")
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();
        startDatePickerU.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar systemCalender = Calendar.getInstance();
                systemCalender.setTimeInMillis(selection);
                int year = systemCalender.get(Calendar.YEAR);
                int monthOfYear = systemCalender.get(Calendar.MONTH)+1;
                int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
                startDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear,year);
                btnStartDateU.setText(startDate);
                startDatePickerU.dismiss();
            }
        });
        endDatePickerU.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar systemCalender = Calendar.getInstance();
                systemCalender.setTimeInMillis(selection);
                int year = systemCalender.get(Calendar.YEAR);
                int monthOfYear = systemCalender.get(Calendar.MONTH)+1;
                int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
                endDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear,year);
                btnEndDateU.setText(endDate);
                endDatePickerU.dismiss();
            }
        });
        startTimePickerU.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minute=startTimePickerU.getMinute();
                int hour=startTimePickerU.getHour();
                startTime = String.format("%02d:%02d", hour, minute);
                tvStartTimeU.setVisibility(View.VISIBLE);
                btnStartTimeU.setText(startTime);
                startTimePickerU.dismiss();
            }
        });
        endTimePickerU.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minute=endTimePickerU.getMinute();
                int hour=endTimePickerU.getHour();
                endTime = String.format("%02d:%02d", hour, minute);
                btnEndTimeU.setText(endTime);
                endTimePickerU.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==btnStartTimeU){
            startTimePickerU.show(this.getSupportFragmentManager(),"StartTimePicker");
        }
        if (v==btnEndTimeU){
            endTimePickerU.show(this.getSupportFragmentManager(),"EndTimePicker");

        }
        if (v==btnStartDateU){
            startDatePickerU.show(this.getSupportFragmentManager(),"StartDatePicker");

        }
        if (v==btnEndDateU){
            endDatePickerU.show(this.getSupportFragmentManager(),"EndDatePicker");//פותח גם דייט וגם טיים והטיים נשמר רק בפעם השנייה שמפעילים את הפיקרים
        }
        if (v==btnUpdateShift){
            tipS=etTipU.getText().toString();
           //לעשות תקינות קלט לתאריכים
            if(tipS.equals("")){
                tip=0;
            }
            else{
                Earning earn = new Earning(startDate,startTime,endDate,endTime,money,tip);
                money=earn.calculateMinutes()*(wage/60);
                earn.setMoney(money);
                tip=Double.parseDouble(tipS);
                earn.setTip(tip);
                earn.setId(id);
                eoh.open();
                eoh.UpdateEarning(earn);
                eoh.close();
                Toast.makeText(this,"המשמרת עודכנה! ",Toast.LENGTH_LONG).show();
                Intent i= new Intent(this, EarningScreen.class);
                startActivity(i);
            }
        }
        if(v==btnDeleteShift) {
            eoh.open();
            eoh.deleteById(id);
            eoh.close();
            Toast.makeText(this, "המשמרת נמחקה! ", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, EarningScreen.class);
        }
        }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==cbTipU&&isChecked==true){
            layTipU.setVisibility(View.VISIBLE);
        }
        if(buttonView==cbTipU&&isChecked==false){
            layTipU.setVisibility(View.GONE);
        }
    }
}