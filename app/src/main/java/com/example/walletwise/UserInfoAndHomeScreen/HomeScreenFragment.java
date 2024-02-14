package com.example.walletwise.UserInfoAndHomeScreen;

import static android.app.PendingIntent.getActivity;

import static java.lang.String.format;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeScreenFragment extends Fragment implements View.OnClickListener {
    private TextView tvUserName, tvCurDate, tvCurCashState, tvCurCashStateNum, tvCurMonthEarningsNum, tvCurMonthSpendingsNum, tvState, tvStateP2;
    private String[] Months, types;
    private Calendar c = Calendar.getInstance();
    private int curMonth = c.get(Calendar.MONTH), curYear = c.get(Calendar.YEAR);
    private String name = "", curMonthDisplay = "", DateToDisplay = "", curYearS = Integer.toString(curYear), curMonthS = "", spendSumS = "", earnSumS = "", sumstateS = "";
    private FloatingActionButton fabNextMonth1, fabPrevMonth1;
    private SpendingsOpenHelper soh;
    private EarningsOpenHelper eoh;
    private PieChart pieChart;
    private Dialog dialog;
    private double sumSpend = 0, sumEarn = 0, sumState = 0;
    private int[] colorValues;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        soh = new SpendingsOpenHelper(getActivity());
        eoh = new EarningsOpenHelper(getActivity());
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

//להעביר תרשים עוגה למסך הוצאות

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

    }
    public void pieChartSetUp() {
        PieDataSet pieDataSet = new PieDataSet(TypesDataByDate(curMonthS, curYearS, types), "data set 1");
        pieDataSet.setColors(colorValues);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setSliceSpace(3);
        PieData pieData = new PieData(pieDataSet);
        pieChart.clear();
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterText("מידע מפורט על ההוצאות ב" + Months[curMonth] +" "+  curYearS);
        pieChart.setCenterTextSize(18);
        pieChart.setCenterTextRadiusPercent(80);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setTransparentCircleAlpha(20);

    }

    private ArrayList<PieEntry> TypesDataByDate(String month, String year, String[] types) {
        ArrayList<PieEntry> dataVals = new ArrayList<PieEntry>();
        int count = soh.countAllSpendingsByDate(month, year);
        for (int i = 0; i < types.length; i++) {
            if(soh.ExistTypeByDate(month,year,types[i])){
                int typeCount = soh.getCountOfSpendTypeAndDate(month, year, types[i]);
                double precent = (typeCount / count) * 100;
                dataVals.add(new PieEntry((float) precent, types[i]));
            }

        }
        return dataVals;
    }



}
