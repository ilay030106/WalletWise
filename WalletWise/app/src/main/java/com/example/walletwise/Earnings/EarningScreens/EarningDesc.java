package com.example.walletwise.Earnings.EarningScreens;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION_CODES.P;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.R;
import com.example.walletwise.UserInfoAndHomeScreen.SetUpActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;


public class EarningDesc extends BottomSheetDialogFragment {

    String earning="",payment="",tips="",curYearS="",curMonthS="",monthlyHours="";
    int curYear,curMonth,hours,minutes;
    EarningsOpenHelper eoh;






    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_earning_desc, container, false);
        eoh= new EarningsOpenHelper(this.getActivity());
        SharedPreferences info=  this.getActivity().getSharedPreferences("earnInfo", MODE_PRIVATE);
        TextView tvHoursNum=v.findViewById(R.id.tvHoursNum);
        TextView tvHours=v.findViewById(R.id.tvHours);
        TextView tvPaymentNum=v.findViewById(R.id.tvPaymentNum);
        TextView tvPayment=v.findViewById(R.id.tvPayment);
        TextView tvTipsNum=v.findViewById(R.id.tvTipsNum);
        TextView tvTips=v.findViewById(R.id.tvTips);
        TextView tvEarningNum=v.findViewById(R.id.tvEarningNum);
        TextView tvEarning=v.findViewById(R.id.tvEarning);
        curYear =info.getInt("year",curYear);
        curYearS = Integer.toString(curYear);
        //curMonth1=curMonth+1;
        curMonth=info.getInt("month",curMonth);
        curMonthS=Integer.toString(curMonth);
        hours= eoh.calculateHoursbyDate(curMonthS,curYearS);
        minutes= eoh.calculateMinutesbyDate(curMonthS,curYearS);
        monthlyHours = String.format("%02d:%02d", hours, minutes);
        earning=Double.toString(eoh.getSumEarningByDate(curMonthS,curYearS));
        payment=Double.toString(eoh.getSumPaymentByDate(curMonthS,curYearS));
        tips=Double.toString(eoh.getSumTipsByDate(curMonthS,curYearS));
        tvHoursNum.setText(monthlyHours);
        tvPaymentNum.setText(payment);
        tvTipsNum.setText(tips);
        tvEarningNum.setText(earning);


        return v;

    }


}