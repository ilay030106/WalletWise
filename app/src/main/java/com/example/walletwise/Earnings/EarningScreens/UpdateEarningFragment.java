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

public class UpdateEarningFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    long id = 0;
    private TextView tvStartDateU, tvStartTimeU, tvEndDateU, tvEndTimeU;
    private MaterialButton btnStartDateU, btnEndDateU, btnStartTimeU, btnEndTimeU, btnUpdateShift, btnDeleteShift;
    private CheckBox cbTipU;
    private TextInputEditText etTipU;
    private TextInputLayout layTipU;
    private double money = 0, tip = 0, wage = 0;

    private FloatingActionButton fabCloseEarn;
    private String startDate = "", endDate = "", startTime = "", endTime = "", wageS = "", tipS = "";
    private MaterialTimePicker startTimePickerU, endTimePickerU;
    private MaterialDatePicker startDatePickerU, endDatePickerU;
    private EarningsOpenHelper eoh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_earning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eoh = new EarningsOpenHelper(requireActivity());
        SharedPreferences getWage = requireActivity().getSharedPreferences("setUp", Context.MODE_PRIVATE);
        wageS = getWage.getString("wage", " ");
        wage = Double.parseDouble(wageS);

        tvStartDateU = view.findViewById(R.id.tvStartDateU);
        tvStartTimeU = view.findViewById(R.id.tvStartDateU);
        tvEndDateU = view.findViewById(R.id.tvStartDateU);
        tvEndTimeU = view.findViewById(R.id.tvStartDateU);
        btnStartDateU = view.findViewById(R.id.btnStartDateU);
        btnStartDateU.setOnClickListener(this);
        btnEndDateU = view.findViewById(R.id.btnEndDateU);
        btnEndDateU.setOnClickListener(this);
        btnStartTimeU = view.findViewById(R.id.btnStartTimeU);
        btnStartTimeU.setOnClickListener(this);
        btnEndTimeU = view.findViewById(R.id.btnEndTimeU);
        btnEndTimeU.setOnClickListener(this);
        btnUpdateShift = view.findViewById(R.id.btnUpdateShift);
        btnUpdateShift.setOnClickListener(this);
        btnDeleteShift = view.findViewById(R.id.btnDeleteShift);
        btnDeleteShift.setOnClickListener(this);
        fabCloseEarn = view.findViewById(R.id.fabCloseEarnU);
        fabCloseEarn.setOnClickListener(this);
        cbTipU = view.findViewById(R.id.cbTipU);
        cbTipU.setOnCheckedChangeListener(this);
        etTipU = view.findViewById(R.id.etTipU);
        layTipU = view.findViewById(R.id.layTipU);
        etTipU.setHintTextColor(getResources().getColor(R.color.lightGrey));
        cbTipU.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getLong("id");
        }
        if (id != 0) {
            eoh.open();
            Earning c = eoh.getEarningByID(id);
            eoh.close();
            btnStartDateU.setText(c.getStartDate());
            btnStartTimeU.setText(c.getStartTime());
            btnEndDateU.setText(c.getEndDate());
            btnEndTimeU.setText(c.getEndTime());
            if (c.getTip() > 0) {
                cbTipU.setChecked(true);
                layTipU.setVisibility(View.VISIBLE);
                etTipU.setText(Double.toString(c.getTip()));
            }
        }

        startTimePickerU = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("עדכן את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();

        endTimePickerU = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("עדכן את שעת תחילת המשמרת")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();

        startDatePickerU = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("עדכן את תאריך תחילת המשמרת")
                .setPositiveButtonText("עדכן")
                .setNegativeButtonText("בטל")
                .build();

        startDatePickerU.addOnPositiveButtonClickListener(selection -> {
            Calendar systemCalender = Calendar.getInstance();
            systemCalender.setTimeInMillis((Long) selection);
            int year = systemCalender.get(Calendar.YEAR);
            int monthOfYear = systemCalender.get(Calendar.MONTH) + 1;
            int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
            startDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear, year);
            btnStartDateU.setText(startDate);
            startDatePickerU.dismiss();
            tvStartDateU.setVisibility(View.VISIBLE);
            tvEndDateU.setVisibility(View.VISIBLE);
            btnEndDateU.setVisibility(View.VISIBLE);

            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from((Long) selection))
                    .build();

            endDatePickerU = MaterialDatePicker.Builder.datePicker()
                    .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                    .setTitleText("בחר את תאריך סוף המשמרת")
                    .setCalendarConstraints(constraints)
                    .build();

            endDatePickerU.addOnPositiveButtonClickListener(endSelection -> {
                Calendar endSystemCalender = Calendar.getInstance();
                endSystemCalender.setTimeInMillis((Long) endSelection);
                int endYear = endSystemCalender.get(Calendar.YEAR);
                int endMonthOfYear = endSystemCalender.get(Calendar.MONTH) + 1;
                int endDayOfMonth = endSystemCalender.get(Calendar.DAY_OF_MONTH);
                tvEndDateU.setVisibility(View.VISIBLE);
                endDate = String.format("%02d/%02d/%d", endDayOfMonth, endMonthOfYear, endYear);
                btnEndDateU.setText(endDate);
                endDatePickerU.dismiss();
            });
        });

        startTimePickerU.addOnPositiveButtonClickListener(v -> {
            int minute = startTimePickerU.getMinute();
            int hour = startTimePickerU.getHour();
            startTime = String.format("%02d:%02d", hour, minute);
            tvStartTimeU.setVisibility(View.VISIBLE);
            btnStartTimeU.setText(startTime);
            startTimePickerU.dismiss();
            btnEndTimeU.setVisibility(View.VISIBLE);
            tvEndTimeU.setVisibility(View.VISIBLE);
        });

        endTimePickerU.addOnPositiveButtonClickListener(v -> {
            int minute = endTimePickerU.getMinute();
            int hour = endTimePickerU.getHour();
            endTime = String.format("%02d:%02d", hour, minute);
            btnEndTimeU.setText(endTime);
            endTimePickerU.dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnStartTimeU) {
            startTimePickerU.show(requireActivity().getSupportFragmentManager(), "StartTimePicker");
        }
        if (v == btnEndTimeU) {
            endTimePickerU.show(requireActivity().getSupportFragmentManager(), "EndTimePicker");
        }
        if (v == btnStartDateU) {
            startDatePickerU.show(requireActivity().getSupportFragmentManager(), "StartDatePicker");
        }
        if (v == btnEndDateU) {
            endDatePickerU.show(requireActivity().getSupportFragmentManager(), "EndDatePicker");
        }
        if (v == btnUpdateShift) {
            tipS = etTipU.getText().toString();
            if (tipS.equals("")) {
                tip = 0;
            } else {
                Earning earn = new Earning(startDate, startTime, endDate, endTime, money, tip);
                money = earn.calculateMinutes() * (wage / 60);
                earn.setMoney(money);
                tip = Double.parseDouble(tipS);
                earn.setTip(tip);
                earn.setId(id);
                eoh.open();
                eoh.UpdateEarning(earn);
                eoh.close();
                Toast.makeText(requireActivity(), "המשמרת עודכנה! ", Toast.LENGTH_LONG).show();
                AppScreen appScreen = (AppScreen) getActivity();
                appScreen.replaceFragment(new EarningListFragment());
            }
        }
        if (v == btnDeleteShift) {
            eoh.open();
            eoh.deleteById(id);
            eoh.close();
            Toast.makeText(requireActivity(), "המשמרת נמחקה! ", Toast.LENGTH_LONG).show();
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
        if (buttonView == cbTipU && isChecked) {
            layTipU.setVisibility(View.VISIBLE);
        }
        if (buttonView == cbTipU && !isChecked) {
            layTipU.setVisibility(View.GONE);
        }
    }
}
