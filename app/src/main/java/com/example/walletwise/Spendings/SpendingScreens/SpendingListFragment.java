package com.example.walletwise.Spendings.SpendingScreens;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletwise.R;
import com.example.walletwise.RecyclerItemClickListener;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingAdapter;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.UserInfoAndHomeScreen.AppScreen;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SpendingListFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, NumberPicker.OnValueChangeListener, SearchView.OnQueryTextListener {
    MaterialButton btnToggle, btnClear;
    TextInputLayout MinPriceLay, MaxPriceLay,typeLayoutFilter;
    TextInputEditText etMinPrice, etMaxPrice;
    CheckBox cbDate, cbSpendType, cbPrice;
    SpendingsOpenHelper soh;
    List<Spending> listOfSpendings;
    RecyclerView recyclerView;
    SpendingAdapter spendingAdapter;
    SearchBar sbSearch;
    Dialog dialog;
    String year = "", month = "", type = "", curMonthDisplay = "", CurYearS = "",CurMonthS = "",DateToDisplay = "";
    int curMonth = 0,curYear=0;
    double max, min,sumSpend = 0;
    NumberPicker npMonths, npYears;
    String[] months, Types,Months;
    Calendar c ;
    PieChart pieChart;
    int[] colorValues;
    String textToDisplay="";
    FloatingActionButton fabCloseSpendInfo,fabNextMonth2, fabPrevMonth2,fabFilters;
    TextView curDate1,infoToDisplay;
    MaterialAutoCompleteTextView SpendTypesDropMenu;
    boolean cbSpendTypeCheck=false,cbDateCheck=false;
    ArrayAdapter TypesAdapter;
    String []types;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spending_list, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recyclerViewS);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);
        soh = new SpendingsOpenHelper(requireContext());
        listOfSpendings = new ArrayList<>();
        c = Calendar.getInstance();
        curMonth = c.get(Calendar.MONTH);
        CurMonthS=Integer.toString(curMonth);
        curYear=c.get(Calendar.YEAR);
        CurYearS = Integer.toString(curYear);
        infoToDisplay=view.findViewById(R.id.infoToDisplay);
        fabFilters=view.findViewById(R.id.fabFilters);
        fabFilters.setOnClickListener(this);
        soh.open();
        listOfSpendings = soh.searchSpendings(String.format("%02d", curMonth + 1), CurYearS, "", -1, -1);
        soh.close();
        spendingAdapter = new SpendingAdapter(requireContext(), listOfSpendings);
        recyclerView.setAdapter(spendingAdapter);
        //sbSearch = view.findViewById(R.id.sbSearch);
        //sbSearch.setOnQueryTextListener(this);
        months = getResources().getStringArray(R.array.months);
        curMonthDisplay = months[curMonth];
        infoToDisplay.setText(curMonthDisplay+ " " +curYear);
        Types = getResources().getStringArray(R.array.types);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(requireContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long id = ((SpendingAdapter) recyclerView.getAdapter()).getSpendingId(position);
                sendIdtoUpdate(id);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                showDeleteDialog(((SpendingAdapter) recyclerView.getAdapter()).getSpendingId(position));
            }
        }));
        sumSpend = soh.getSumSpendingByDate(CurMonthS, CurYearS);
        colorValues =new int[] {getResources().getColor(R.color.lightBlue),getResources().getColor(R.color.babyBlue),getResources().getColor(R.color.egyptianBlue),getResources().getColor(R.color.blueishWhite),getResources().getColor(R.color.darkBlue),getResources().getColor(R.color.Turquoise),getResources().getColor(R.color.brightBlue),getResources().getColor(R.color.Glaucous)};
        Months = getResources().getStringArray(R.array.months);
        DividerItemDecoration divider= new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.item_divider));
        recyclerView.addItemDecoration(divider);
        FilterMenuLayout layout=(FilterMenuLayout) view.findViewById(R.id.filter_menu_spendings);
        FilterMenu menu= new FilterMenu.Builder(getActivity())
                .inflate(R.menu.filter_menu)
                .attach(layout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onMenuItemClick(View view, int position) {
                        if(position==1){
                            AppScreen appScreen = (AppScreen) getActivity();
                            appScreen.replaceFragment(new AddSpendingFragment());
                        }
                        if(position==0){
                            if(sumSpend>0)
                                showSpendInfoDialog();
                            else
                                Toast.makeText(getActivity(), "אתה צריך להוציא כסף בשביל לראות תיאור מפורט", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onMenuCollapse() {

                    }

                    @Override
                    public void onMenuExpand() {

                    }
                })
                .build();    }

    private void showDeleteDialog(long spendingId) {
    }

    private void showSpendInfoDialog() {
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.spend_info);
        pieChart = dialog.findViewById(R.id.pieChart);
        fabCloseSpendInfo = dialog.findViewById(R.id.fabCloseSpendInfo);
        fabNextMonth2 = dialog.findViewById(R.id.fabNextMonth2);
        fabPrevMonth2 = dialog.findViewById(R.id.fabPrevMonth2);
        curDate1 = dialog.findViewById(R.id.tvCurDate1);
        fabCloseSpendInfo.setOnClickListener(this);
        fabNextMonth2.setOnClickListener(this);
        fabPrevMonth2.setOnClickListener(this);
        //pieChartSetUp();
        dialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void showFilterDialog() {
        type = "";
        dialog = new Dialog(requireContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.spend_filter);
        cbDate = dialog.findViewById(R.id.cbDate);
        cbDate.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.lightBlue)));
        cbSpendType = dialog.findViewById(R.id.cbSpendType);
        cbSpendType.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.lightBlue)));
        cbPrice = dialog.findViewById(R.id.cbPrice);
        cbPrice.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.lightBlue)));
        btnToggle = dialog.findViewById(R.id.btnToggle);
        npMonths = dialog.findViewById(R.id.npMonths);
        npYears = dialog.findViewById(R.id.npYears);
        typeLayoutFilter = dialog.findViewById(R.id.typeLayoutFilter);
        SpendTypesDropMenu = dialog.findViewById(R.id.SpendTypesDropMenu);
        etMaxPrice = dialog.findViewById(R.id.etMaxPrice);
        etMinPrice = dialog.findViewById(R.id.etMinPrice);
        btnClear = dialog.findViewById(R.id.btnClear);
        MinPriceLay = dialog.findViewById(R.id.MinPriceLay);
        MaxPriceLay = dialog.findViewById(R.id.MaxPriceLay);
        npYears.setMinValue(2021);
        npYears.setMaxValue(2030);
        npYears.setValue(c.get(Calendar.YEAR));
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
        types = getResources().getStringArray(R.array.types);
        TypesAdapter = new ArrayAdapter<>(getActivity(), R.layout.type_dropdown_item, types);
        SpendTypesDropMenu.setAdapter(TypesAdapter);
        SpendTypesDropMenu.setOnItemClickListener((adapterView, view1, i, l) ->
                type = adapterView.getItemAtPosition(i).toString());
        dialog.show();
        month = "";
        year = "";

        npMonths.setTextColor(requireContext().getColor(R.color.black));
        npYears.setTextColor(requireContext().getColor(R.color.black));
        //להעביר את הכפורים של העברת חודשים לדיאלוג הזה
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View v) {
        if (v == btnToggle) {
            dialog.dismiss();
            String maxStr = etMaxPrice.getText().toString();
            String minStr = etMinPrice.getText().toString();
            type=SpendTypesDropMenu.getText().toString();
            if (maxStr.length() == 0) {
                
                max = -1;
            } else {
                max = Double.parseDouble(maxStr);
                textToDisplay+= "מחיר מקסימלי: " + max +"\n";

            }
            if (minStr.length() == 0) {
                min = -1;
            } else {
                min = Double.parseDouble(minStr);
                textToDisplay+= "מחיר מינימלי: " + min +"\n";

            }
          /*  if(!month.equals("")&&!year.equals("")){
                textToDisplay+= "תאריך: " + curMonthDisplay +" " +year +"\n";

            }*/
            if(!type.equals("")){
                textToDisplay+= "סוג הוצאה: " + type +"\n";

            }
            infoToDisplay.setText(textToDisplay);
            listOfSpendings.clear();
            soh.open();
            listOfSpendings.addAll(soh.searchSpendings(month, year, type, min, max));
            soh.close();
            spendingAdapter = new SpendingAdapter(requireContext(), listOfSpendings);
            recyclerView.setAdapter(spendingAdapter);
            
        }

        if (v == btnClear) {
            soh.open();
            listOfSpendings.clear();
            listOfSpendings.addAll(soh.getAllSpendings());
            soh.close();
            dialog.dismiss();
            spendingAdapter = new SpendingAdapter(requireContext(), listOfSpendings);
            recyclerView.setAdapter(spendingAdapter);
            infoToDisplay.setText("כל ההוצאות");
        }
        if (v == fabNextMonth2) {
            curMonth++;
            if (curMonth == 12) {
                curMonth = 0;
                curYear++;
            }
            CurYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            CurMonthS = format("%02d", curMonth + 1);
            DateToDisplay = curMonthDisplay + " " + CurYearS;
            curDate1.setText(DateToDisplay);
        }
        if (v == fabPrevMonth2) {
            curMonth--;
            if (curMonth == -1) {
                curMonth = 11;
                curYear--;
            }
            CurYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            CurMonthS = format("%02d", curMonth + 1);
            DateToDisplay = curMonthDisplay + " " + CurYearS;
            sumSpend = soh.getSumSpendingByDate(CurMonthS, CurYearS);
            curDate1.setText(DateToDisplay);

        }
        if (v == fabCloseSpendInfo) {
            dialog.dismiss();
        }
        if(v==fabFilters){
            showFilterDialog();
        }

    }
    @Override
    public void onCheckedChanged(CompoundButton cb, boolean b) {
        if (cb == cbPrice && b) {
            MaxPriceLay.setVisibility(View.VISIBLE);
            MinPriceLay.setVisibility(View.VISIBLE);

        }

        if (cb == cbPrice && !b) {
            MaxPriceLay.setVisibility(View.GONE);
            MinPriceLay.setVisibility(View.GONE);
        }
        if (cb == cbSpendType && b) {
            typeLayoutFilter.setVisibility(View.VISIBLE);
            cbSpendTypeCheck=true;
        }

        if (cb == cbSpendType && !b) {
            typeLayoutFilter.setVisibility(View.GONE);
            cbSpendTypeCheck=false;
        }
        if (cb == cbDate && b) {
            npMonths.setVisibility(View.VISIBLE);
            npYears.setVisibility(View.VISIBLE);
            cbDateCheck=true;
        }

        if (cb == cbDate && !b) {
            npYears.setVisibility(View.GONE);
            npMonths.setVisibility(View.GONE);
            cbDateCheck=false;

        }

    }
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int newVal) {
        if (numberPicker == npMonths) {
            month = String.format("%02d", newVal + 1);
        }
        if (numberPicker == npYears) {
            year = Integer.toString(newVal);
        }



    }
    public void sendIdtoUpdate(long id) {
        Bundle idB = new Bundle();
        idB.putLong("id", id);

        UpdateSpendingFragment usf = new UpdateSpendingFragment();
        usf.setArguments(idB);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.AppFragmentContainer, usf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //sbSearch=(SearchBar) menu.findItem(R.id.search).getActionView();
        //sbSearch.setQueryHint("אנא ציין את תיאור ההוצאה");
        //sbSearch.setOnQueryTextListener(this);
    }




    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

   /* public void pieChartSetUp() {
        PieDataSet pieDataSet = new PieDataSet(TypesDataByDate(CurMonthS, CurYearS, types), "data set 1");
        pieDataSet.setColors(colorValues);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setSliceSpace(3);
        PieData pieData = new PieData(pieDataSet);
        pieChart.clear();
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterText("מידע מפורט על ההוצאות ב" + Months[curMonth] +" "+  CurYearS);
        pieChart.setCenterTextSize(18);
        pieChart.setCenterTextRadiusPercent(80);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setTransparentCircleAlpha(20);

    }*/

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