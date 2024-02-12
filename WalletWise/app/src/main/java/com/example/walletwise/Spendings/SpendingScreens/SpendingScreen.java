package com.example.walletwise.Spendings.SpendingScreens;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;

import com.example.walletwise.Earnings.EarningScreens.EarningScreen;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingAdd;
import com.example.walletwise.UserInfoAndHomeScreen.HomeScreen;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingAdapter;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class SpendingScreen extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, SearchView.OnQueryTextListener, BottomNavigationView.OnNavigationItemSelectedListener, NumberPicker.OnValueChangeListener {
    Button  btnToggle, btnClear;
    MaterialButton btnAddSpending;

    TextInputLayout MinPriceLay,MaxPriceLay;
    TextInputEditText etMinPrice, etMaxPrice;
    CheckBox cbDate, cbSpendType, cbPrice;
    SpendingsOpenHelper soh;
    ArrayList<Spending> listOfSpendings;
    ListView listView;
    SpendingAdapter spendingAdapter;
    FloatingActionButton fabFilters;
    SearchView sbSearch;
    Dialog dialog;
    String Years[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String year = "", month = "", type = "", curMonthDisplay = "", curYear = "", textToDisplay = "";
    int curMonth = 0;
    double max, min;
    NumberPicker npMonths,npYears,npSpendTypes;
    String[] months,Types;
    BottomNavigationView bnv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_screen);
        listView = findViewById(R.id.listviewS);
        soh = new SpendingsOpenHelper(this);
        listOfSpendings = new ArrayList<Spending>();
        Calendar c = Calendar.getInstance();
        curMonth = c.get(Calendar.MONTH);
        curYear = Integer.toString(c.get(Calendar.YEAR));
        soh.open();
        listOfSpendings = soh.searchSpendings(Integer.toString(curMonth+1), curYear, "", -1, -1);//לבדוק למה בהתחלה הוא לא מציג ישר את הבזבוזים של אותו חודש
        //listOfSpendings=soh.getAllSpendings();
        soh.close();
        spendingAdapter = new SpendingAdapter(this, 0, listOfSpendings);
        listView.setAdapter(spendingAdapter);
        btnAddSpending = findViewById(R.id.btnAddSpending);
        fabFilters = findViewById(R.id.fabFilters);
        sbSearch = findViewById(R.id.sbSearch);
        fabFilters.setOnClickListener(this);
        btnAddSpending.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        sbSearch.setOnQueryTextListener(this);
        bnv = findViewById(R.id.bottom_navigation_spendings);
        bnv.setSelectedItemId(R.id.action_spending_tracker);
        bnv.setOnItemSelectedListener(this);
        //textToDisplay = curMonthDisplay + " " + curYear;
        //tvFilterDesc.setText(textToDisplay);
        //לעשות שהטקסט ישתנה לפי הפילטרים שנבחרו
        months=getResources().getStringArray(R.array.months);
        curMonthDisplay = months[curMonth];
        Types=getResources().getStringArray(R.array.types);
        Double sum=soh.getSumSpendingByDate("11","2023");
        bnv.setItemIconTintList(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    public void onClick(View v) {
        if (v == btnAddSpending) {
            Intent i = new Intent(this, SpendingAdd.class);
            startActivity(i);
        }

        if (v == fabFilters) {
            dialog = new Dialog(this);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.spend_filter);
            cbDate = dialog.findViewById(R.id.cbDate);
            cbDate.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
            cbSpendType = dialog.findViewById(R.id.cbSpendType);
            cbSpendType.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
            cbPrice = dialog.findViewById(R.id.cbPrice);
            cbPrice.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
            btnToggle = dialog.findViewById(R.id.btnToggle);
            npMonths = dialog.findViewById(R.id.npMonths);
            npYears = dialog.findViewById(R.id.npYears);
            npSpendTypes = dialog.findViewById(R.id.npSpendTypes);
            etMaxPrice = dialog.findViewById(R.id.etMaxPrice);
            etMinPrice = dialog.findViewById(R.id.etMinPrice);
            btnClear = dialog.findViewById(R.id.btnClear);
            MinPriceLay = dialog.findViewById(R.id.MinPriceLay);
            MaxPriceLay = dialog.findViewById(R.id.MaxPriceLay);
            npYears.setMinValue(2021);
            npYears.setMaxValue(2030);
            npMonths.setMinValue(0);
            npMonths.setMaxValue(11);
            npMonths.setOnValueChangedListener(this);
            npYears.setOnValueChangedListener(this);
            npMonths.setDisplayedValues(months);
            cbDate.setOnCheckedChangeListener(this);
            cbSpendType.setOnCheckedChangeListener(this);
            cbPrice.setOnCheckedChangeListener(this);
            btnToggle.setOnClickListener(this);
            btnClear.setOnClickListener(this);
            npSpendTypes.setMinValue(0);
            npSpendTypes.setMaxValue(7);
            npSpendTypes.setValue(0);
            npSpendTypes.setOnValueChangedListener(this);
            npSpendTypes.setDisplayedValues(Types);
            dialog.show();
            month = "";
            year = "";
            type = "";
            npMonths.setTextColor(getColor(R.color.white));
            npYears.setTextColor(getColor(R.color.white));
            npSpendTypes.setTextColor(getColor(R.color.white));
        }
        if (v == btnToggle) {
            dialog.dismiss();
            String maxStr = etMaxPrice.getText().toString();
            String minStr = etMinPrice.getText().toString();
            if (maxStr.length() == 0) {
                max = -1;
            } else {
                max = Double.parseDouble(maxStr);
            }
            if (minStr.length() == 0) {
                min = -1;
            } else {
                min = Double.parseDouble(minStr);
            }
            listOfSpendings.clear();
            soh.open();
            listOfSpendings.addAll(soh.searchSpendings(month, year, type, min, max));
            soh.close();
            spendingAdapter = new SpendingAdapter(this, 0, listOfSpendings);
            listView.setAdapter(spendingAdapter);

        }
        if (v == btnClear) {
            soh.open();
            listOfSpendings.clear();
            listOfSpendings.addAll(soh.getAllSpendings());
            soh.close();
            dialog.dismiss();
            spendingAdapter = new SpendingAdapter(this, 0, listOfSpendings);
            listView.setAdapter(spendingAdapter);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Spending spending = spendingAdapter.getItem(position);
        Intent i = new Intent(SpendingScreen.this, UpdateSpendingActivity.class);
        i.putExtra( "rowId", spending.getId());
        startActivity(i);
    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean b) {
        if (cb == cbDate && b) {
            npMonths.setVisibility(View.VISIBLE);
            npYears.setVisibility(View.VISIBLE);

        }
        if (cb == cbDate && !b) {
            npYears.setVisibility(View.GONE);
            npMonths.setVisibility(View.GONE);
        }
        if (cb == cbSpendType && b) {
            npSpendTypes.setVisibility(View.VISIBLE);
        }
        if (cb == cbSpendType && !b) {
            npSpendTypes.setVisibility(View.GONE);
        }
        if (cb == cbPrice && b) {
            MaxPriceLay.setVisibility(View.VISIBLE);
            MinPriceLay.setVisibility(View.VISIBLE);
        }
        if (cb == cbPrice && !b) {
            MaxPriceLay.setVisibility(View.GONE);
            MinPriceLay.setVisibility(View.GONE);
        }


    }




    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //spendingAdapter.filter(s);
        return false;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            startActivity(new Intent(this, HomeScreen.class));
            return true;
        } else if (item.getItemId() == R.id.action_spending_tracker) {
            // Handle spending tracker action
            return true;

        } else if (item.getItemId() == R.id.action_wage_counter) {
            startActivity(new Intent(this, EarningScreen.class)); // Change to the appropriate activity
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onValueChange(NumberPicker np, int oldVal, int newVal) {
        if(np==npMonths){
            month = Integer.toString(newVal + 1);
        }
        if(np==npYears){
            year =Integer.toString(newVal);
        }
        if(np==npSpendTypes){
            type=Types[newVal];
        }

    }

}

