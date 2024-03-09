package com.example.walletwise.UserInfoAndHomeScreen;

import static android.app.PendingIntent.getActivity;

import static java.lang.String.format;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeScreenFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    TextView tvUserName, tvCurDate, tvCurCashState, tvCurCashStateNum, tvCurMonthEarningsNum, tvCurMonthSpendingsNum, tvState, tvStateP2,tvChangeInfo;
    String[] Months, types;
     Calendar c = Calendar.getInstance();
    int curMonth = c.get(Calendar.MONTH), curYear = c.get(Calendar.YEAR);
    String name = "", curMonthDisplay = "", DateToDisplay = "", curYearS = Integer.toString(curYear), curMonthS = "", spendSumS = "", earnSumS = "", sumstateS = "";
    FloatingActionButton fabNextMonth1, fabPrevMonth1;
    SpendingsOpenHelper soh;
    EarningsOpenHelper eoh;

    double sumSpend = 0, sumEarn = 0, sumState = 0;
    int[] colorValues;
    Dialog dialog;
    MaterialCheckBox cbChangeWage,cbChangeName;
    TextInputLayout nameLay,wageLay;
    TextInputEditText etNameU,etwageU;
    boolean changeName=false,changeWage=false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        soh = new SpendingsOpenHelper(getActivity());
        eoh = new EarningsOpenHelper(getActivity());
        tvChangeInfo=view.findViewById(R.id.tvChangeInfo);
        tvCurDate = view.findViewById(R.id.tvCurDate);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvCurCashState = view.findViewById(R.id.tvCurCashState);
        tvCurCashStateNum = view.findViewById(R.id.tvCurCashStateNum);
        tvCurMonthEarningsNum = view.findViewById(R.id.tvCurMonthEarningsNum);
        tvCurMonthSpendingsNum = view.findViewById(R.id.tvCurMonthSpendingsNum);
        tvState = view.findViewById(R.id.tvState);
        tvStateP2 = view.findViewById(R.id.tvStateP2);
        fabNextMonth1 = view.findViewById(R.id.fabNextMonth1);
        fabPrevMonth1 = view.findViewById(R.id.fabPrevMonth1);
        SharedPreferences getName = getActivity().getSharedPreferences("setUp", Context.MODE_PRIVATE);
        name = getName.getString("name", "");
        tvUserName.setText(" " + "ברוך הבא " + name);
        fabNextMonth1.setOnClickListener(this);
        fabPrevMonth1.setOnClickListener(this);
        tvChangeInfo.setOnClickListener(this);

        Months = getResources().getStringArray(R.array.months);
        types = getResources().getStringArray(R.array.types);
        curMonthDisplay = Months[curMonth];
        DateToDisplay = curMonthDisplay + " " + curYearS;
        tvCurDate.setText(DateToDisplay);
        curMonthS = String.format("%02d", curMonth + 1);
        sumSpend = soh.getSumSpendingByDate(curMonthS, curYearS);
        sumEarn = eoh.getSumEarningByDate(curMonthS, curYearS);
        spendSumS = Double.toString(sumSpend);
        earnSumS = Double.toString(sumEarn);
        sumState = sumEarn - sumSpend;
        sumstateS = sumState + "₪";
        tvCurMonthSpendingsNum.setText(spendSumS + "₪");
        tvCurMonthEarningsNum.setText(earnSumS + "₪");
        tvCurCashStateNum.setText(sumstateS);
        if (sumState > 0) {
            tvState.setTextColor(getResources().getColor(R.color.green));
            tvState.setText("ברווח ");
            tvCurCashStateNum.setTextColor(getResources().getColor(R.color.green));
            tvCurCashStateNum.setVisibility(View.VISIBLE);
            tvStateP2.setVisibility(View.VISIBLE);

        }
        if (sumState < 0) {
            tvState.setTextColor(getResources().getColor(R.color.red));
            tvState.setText("בהפסד ");
            tvCurCashStateNum.setTextColor(getResources().getColor(R.color.red));
            tvCurCashStateNum.setVisibility(View.VISIBLE);
            tvStateP2.setVisibility(View.VISIBLE);
        }
        if (sumState == 0) {
            tvState.setText("במצב ניטרלי ");
            tvState.setTextColor(getResources().getColor(R.color.black));
            tvCurCashStateNum.setTextColor(getResources().getColor(R.color.black));
            tvCurCashStateNum.setVisibility(View.INVISIBLE);
            tvStateP2.setVisibility(View.INVISIBLE);

        }
        colorValues =new int[] {getResources().getColor(R.color.lightBlue),getResources().getColor(R.color.babyBlue),getResources().getColor(R.color.egyptianBlue),getResources().getColor(R.color.blueishWhite),getResources().getColor(R.color.darkBlue),getResources().getColor(R.color.Turquoise),getResources().getColor(R.color.brightBlue),getResources().getColor(R.color.Glaucous)};

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == fabNextMonth1) {
            curMonth++;
            if (curMonth == 12) {
                curMonth = 0;
                curYear++;
            }
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurDate.setText(DateToDisplay);
            curMonthS = format("%02d", curMonth + 1);
            sumSpend = soh.getSumSpendingByDate(curMonthS, curYearS);
            sumEarn = eoh.getSumEarningByDate(curMonthS, curYearS);
            earnSumS = Double.toString(sumEarn);
            spendSumS = Double.toString(sumSpend);
            sumState = sumEarn - sumSpend;
            sumstateS = "₪" + sumState;
            tvCurMonthSpendingsNum.setText(spendSumS + "₪");
            tvCurMonthEarningsNum.setText(earnSumS + "₪");
            tvCurCashStateNum.setText(sumstateS);
            if (sumState > 0) {
                tvState.setTextColor(getResources().getColor(R.color.green));
                tvState.setText("ברווח ");
                tvCurCashStateNum.setTextColor(getResources().getColor(R.color.green));
                tvCurCashStateNum.setVisibility(View.VISIBLE);
                tvStateP2.setVisibility(View.VISIBLE);
            }
            if (sumState < 0) {
                tvState.setTextColor(getResources().getColor(R.color.red));
                tvState.setText("בהפסד ");
                tvCurCashStateNum.setTextColor(getResources().getColor(R.color.red));
                tvCurCashStateNum.setVisibility(View.VISIBLE);
                tvStateP2.setVisibility(View.VISIBLE);
            }
            if (sumState == 0) {
                tvState.setText("במצב ניטרלי ");
                tvState.setTextColor(getResources().getColor(R.color.black));
                tvCurCashStateNum.setVisibility(View.INVISIBLE);
                tvStateP2.setVisibility(View.INVISIBLE);

            }
        }
        if (v == fabPrevMonth1) {
            curMonth--;
            if (curMonth == -1) {
                curMonth = 11;
                curYear--;
            }
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurDate.setText(DateToDisplay);
            curMonthS = format("%02d", curMonth + 1);
            sumSpend = soh.getSumSpendingByDate(curMonthS, curYearS);
            sumEarn = eoh.getSumEarningByDate(curMonthS, curYearS);
            earnSumS = Double.toString(sumEarn);
            spendSumS = Double.toString(sumSpend);
            sumState = sumEarn - sumSpend;
            sumstateS = "₪" + sumState;
            tvCurMonthSpendingsNum.setText(spendSumS + "₪");
            tvCurMonthEarningsNum.setText(earnSumS + "₪");
            tvCurCashStateNum.setText(sumstateS);
            if (sumState > 0) {
                tvState.setTextColor(getResources().getColor(R.color.green));
                tvState.setText("ברווח ");
                tvCurCashStateNum.setVisibility(View.VISIBLE);
                tvStateP2.setVisibility(View.VISIBLE);
            }
            if (sumState < 0) {
                tvState.setTextColor(getResources().getColor(R.color.red));
                tvState.setText("בהפסד ");
                tvCurCashStateNum.setVisibility(View.VISIBLE);
                tvStateP2.setVisibility(View.VISIBLE);
            }
            if (sumState == 0) {
                tvState.setText("במצב ניטרלי ");
                tvState.setTextColor(getResources().getColor(R.color.black));
                tvCurCashStateNum.setVisibility(View.INVISIBLE);
                tvStateP2.setVisibility(View.INVISIBLE);

            }

        }
        if(v==tvChangeInfo){
            showChangeInfoDialog();
        }
        

    }

    private void showChangeInfoDialog() {
        dialog = new Dialog(requireContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.change_name_or_wage);
        cbChangeWage=dialog.findViewById(R.id.cbChangeWage);
        cbChangeName=dialog.findViewById(R.id.cbChangeName);
        nameLay=dialog.findViewById(R.id.nameLay);
        wageLay=dialog.findViewById(R.id.wageLay);
        etNameU=dialog.findViewById(R.id.etNameU);
        etwageU=dialog.findViewById(R.id.etWageU);
        cbChangeName.setOnCheckedChangeListener(this);
        cbChangeWage.setOnCheckedChangeListener(this);



    }


    @Override
    public void onCheckedChanged(CompoundButton cb, boolean b) {
        if(cb==cbChangeName&&b){
            nameLay.setVisibility(View.VISIBLE);
            changeName=true;
        }
        if(cb==cbChangeName&&!b){
            nameLay.setVisibility(View.GONE);
            changeName=false;

        }
        if(cb==cbChangeWage&&b){
            wageLay.setVisibility(View.VISIBLE);
            changeWage=true;
        }
        if(cb==cbChangeWage&&!b){
            wageLay.setVisibility(View.GONE);
            changeWage=false;

        }


    }
}
