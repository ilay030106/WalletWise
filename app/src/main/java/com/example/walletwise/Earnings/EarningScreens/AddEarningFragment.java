package com.example.walletwise.Earnings.EarningScreens;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletwise.Earnings.EarnOpenHelper.Earning;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.R;
import com.example.walletwise.UserInfoAndHomeScreen.AppScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class AddEarningFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tvStartDate, tvStartTime, tvEndDate, tvEndTime;
    private MaterialButton btnStartDate, btnEndDate, btnStartTime, btnEndTime, btnAddShift;
    private CheckBox cbTip;
    private TextInputEditText etTip;
    private TextInputLayout layTip;
    private double money = 0, tip = 0, wage = 0;

    private FloatingActionButton fabCloseEarn;
    private String startDate = "", endDate = "", startTime = "", endTime = "", wageS = "", tipS = "";
    private MaterialTimePicker startTimePicker, endTimePicker;
    private MaterialDatePicker startDatePicker, endDatePicker;
    private EarningsOpenHelper eoh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_earning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences getWage = requireActivity().getSharedPreferences("setUp", Context.MODE_PRIVATE);
        wageS = getWage.getString("wage", " ");
        wage = Double.parseDouble(wageS);

        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvEndTime = view.findViewById(R.id.tvEndTime);
        btnStartDate = view.findViewById(R.id.btnStartDate);
        btnStartDate.setOnClickListener(this);
        btnEndDate = view.findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(this);
        btnStartTime = view.findViewById(R.id.btnStartTime);
        btnStartTime.setOnClickListener(this);
        btnEndTime = view.findViewById(R.id.btnEndTime);
        btnEndTime.setOnClickListener(this);
        btnAddShift = view.findViewById(R.id.btnAddShift);
        btnAddShift.setOnClickListener(this);
        fabCloseEarn = view.findViewById(R.id.fabCloseEarn);
        fabCloseEarn.setOnClickListener(this);
        cbTip = view.findViewById(R.id.cbTip);
        cbTip.setOnCheckedChangeListener(this);
        etTip = view.findViewById(R.id.etTip);
        layTip = view.findViewById(R.id.layTip);
        etTip.setHintTextColor(getResources().getColor(R.color.lightGrey));
        cbTip.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
        eoh = new EarningsOpenHelper(requireActivity());

        startTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("בחר את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();

        endTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("בחר את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();

        startDatePicker = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("בחר את תאריך תחילת המשמרת")
                .build();

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar systemCalender = Calendar.getInstance();
            systemCalender.setTimeInMillis((Long) selection);
            int year = systemCalender.get(Calendar.YEAR);
            int monthOfYear = systemCalender.get(Calendar.MONTH) + 1;
            int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
            tvStartDate.setVisibility(View.VISIBLE);
            startDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear, year);
            btnStartDate.setText(startDate);
            startDatePicker.dismiss();
            btnEndDate.setVisibility(View.VISIBLE);

            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from((Long) selection))
                    .build();

            endDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                    .setTitleText("בחר את תאריך סוף המשמרת")
                    .setCalendarConstraints(constraints)
                    .build();

            endDatePicker.addOnPositiveButtonClickListener(endSelection -> {
                Calendar endSystemCalender = Calendar.getInstance();
                endSystemCalender.setTimeInMillis((Long) endSelection);
                int endYear = endSystemCalender.get(Calendar.YEAR);
                int endMonthOfYear = endSystemCalender.get(Calendar.MONTH) + 1;
                int endDayOfMonth = endSystemCalender.get(Calendar.DAY_OF_MONTH);
                tvEndDate.setVisibility(View.VISIBLE);
                endDate = String.format("%02d/%02d/%d", endDayOfMonth, endMonthOfYear, endYear);
                btnEndDate.setText(endDate);
                endDatePicker.dismiss();
            });
        });

        startTimePicker.addOnPositiveButtonClickListener(v1 -> {
            int minute = startTimePicker.getMinute();
            int hour = startTimePicker.getHour();
            startTime = String.format("%02d:%02d", hour, minute);
            tvStartTime.setVisibility(View.VISIBLE);
            btnStartTime.setText(startTime);
            startTimePicker.dismiss();
            btnEndTime.setVisibility(View.VISIBLE);
        });

        endTimePicker.addOnPositiveButtonClickListener(v12 -> {
            int minute = endTimePicker.getMinute();
            int hour = endTimePicker.getHour();
            endTime = String.format("%02d:%02d", hour, minute);
            tvEndTime.setVisibility(View.VISIBLE);
            btnEndTime.setText(endTime);
            endTimePicker.dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnStartTime) {
            startTimePicker.show(requireActivity().getSupportFragmentManager(), "StartTimePicker");
        }
        if (v == btnEndTime) {
            endTimePicker.show(requireActivity().getSupportFragmentManager(), "EndTimePicker");
        }
        if (v == btnStartDate) {
            startDatePicker.show(requireActivity().getSupportFragmentManager(), "StartDatePicker");
        }
        if (v == btnEndDate) {
            endDatePicker.show(requireActivity().getSupportFragmentManager(), "EndDatePicker");
        }
        if (v == btnAddShift) {
            eoh.open();
            tip = 0;
            tipS = etTip.getText().toString();
            Earning earn = new Earning(startDate, startTime, endDate, endTime, money, tip);
            money = earn.calculateMinutes() * (wage / 60);

            if (tipS.equals("")) {
                tip = 0;
            } else {
                tip = Double.parseDouble(tipS);
            }

            earn.setTip(tip);
            earn.setMoney(money);
            eoh.createEarning(earn);
            eoh.close();
            Toast.makeText(requireActivity(), "המשמרת נשמרה בהצלחה! ", Toast.LENGTH_LONG).show();
            AppScreen appScreen = (AppScreen) getActivity();
            appScreen.replaceFragment(new EarningListFragment());
        }
        if (v == fabCloseEarn) {
            AppScreen appScreen = (AppScreen) getActivity();
            appScreen.replaceFragment(new EarningListFragment());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == cbTip && isChecked == true) {
            layTip.setVisibility(View.VISIBLE);
        }
        if (buttonView == cbTip && isChecked == false) {
            layTip.setVisibility(View.GONE);
        }
    }
}
