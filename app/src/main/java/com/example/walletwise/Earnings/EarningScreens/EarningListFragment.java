package com.example.walletwise.Earnings.EarningScreens;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletwise.Earnings.EarnOpenHelper.Earning;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningAdapter;
import com.example.walletwise.Earnings.EarnOpenHelper.EarningsOpenHelper;
import com.example.walletwise.R;
import com.example.walletwise.RecyclerItemClickListener;
import com.example.walletwise.UserInfoAndHomeScreen.AppScreen;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class EarningListFragment extends Fragment implements  View.OnClickListener {
    TextView tvCurEarningMonth, tvMonthlyHours, tvMonthlyEarnings, tvHoursNum, tvPaymentNum, tvTipsNum, tvEarningNum;
    String Months[];
    Calendar c = Calendar.getInstance();
    int curMonth, curYear, hours = 0, minutes = 0;
    String curYearS = "", curMonthDisplay = "", DateToDisplay = "", sumEarn = "", monthlyHours = "", sumPay = "", sumTips = "", curMonthS = "", earning = "", payment = "", tips = "";

    FloatingActionButton fabNextMonth, fabPrevMonth, fabInfo, fabAddEarningShift;

    EarningsOpenHelper eoh;
    RecyclerView recyclerView;
    ArrayList<Earning> listOfEarnings;
    EarningAdapter earningAdapter;
    LinearLayout monthlyInfoLay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_earning_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        curMonthS = String.format("%02d", curMonth + 1);
        recyclerView = view.findViewById(R.id.recyclerViewE);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);
        eoh = new EarningsOpenHelper(getActivity());
        listOfEarnings = new ArrayList<>();
        eoh.open();
        listOfEarnings = eoh.searchEarnings(curMonthS, curYearS, "", "", -1, -1, -1, -1);
        eoh.close();
        earningAdapter = new EarningAdapter(getActivity(), listOfEarnings);
        recyclerView.setAdapter(earningAdapter);
        tvCurEarningMonth = view.findViewById(R.id.tvCurEarningMonth);
        fabNextMonth = view.findViewById(R.id.fabNextMonth);
        fabPrevMonth = view.findViewById(R.id.fabPrevMonth);
        tvMonthlyHours = view.findViewById(R.id.tvMonthlyHours);
        tvMonthlyEarnings = view.findViewById(R.id.tvMonthlyEarnings);
        Months = getResources().getStringArray(R.array.months);
        curMonth = c.get(Calendar.MONTH);
        curYear = c.get(Calendar.YEAR);
        curYearS = Integer.toString(curYear);
        curMonthDisplay = Months[curMonth];
        DateToDisplay = curMonthDisplay + " " + curYearS;
        tvCurEarningMonth.setText(DateToDisplay);
        fabPrevMonth.setOnClickListener(this);
        fabNextMonth.setOnClickListener(this);
        curMonthS = Integer.toString(curMonth + 1);
        sumEarn = "₪" + eoh.getSumEarningByDate(curMonthS, curYearS);
        sumPay = "₪" + eoh.getSumPaymentByDate(curMonthS, curYearS);
        sumTips = "₪" + eoh.getSumTipsByDate(curMonthS, curYearS);
        hours = eoh.calculateHoursbyDate(curMonthS, curYearS);
        minutes = eoh.calculateMinutesbyDate(curMonthS, curYearS);
        monthlyHours = String.format("%02d:%02d", hours, minutes);
        tvMonthlyEarnings.setText(" " + sumEarn);
        tvMonthlyHours.setText(monthlyHours + "שעות");
        SharedPreferences earnInfo = getActivity().getSharedPreferences("earnInfo", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor earnInfoEdit = earnInfo.edit();
        earnInfoEdit.putString("month", curMonthS);
        earnInfoEdit.putInt("year", curYear);
        earnInfoEdit.apply();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long id = ((EarningAdapter) recyclerView.getAdapter()).getEarningId(position);
                sendIdtoUpdate(id);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        DividerItemDecoration divider= new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.item_divider));
        recyclerView.addItemDecoration(divider);
        monthlyInfoLay=view.findViewById(R.id.MonthlyInfoLay);
        FilterMenuLayout layout=(FilterMenuLayout) view.findViewById(R.id.filter_menuEarnings);
        FilterMenu menu= new FilterMenu.Builder(getActivity())
                .inflate(R.menu.filter_menu)
                .attach(layout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int position) {
                        if(position==1){
                            AppScreen appScreen = (AppScreen) getActivity();
                            appScreen.replaceFragment(new AddEarningFragment());
                        }
                        if(position==0){
                            showBottomDialog();
                        }
                    }
                    @Override
                    public void onMenuCollapse() {
                        monthlyInfoLay.setVisibility(View.GONE);
                    }
                    @Override
                    public void onMenuExpand() {
                        monthlyInfoLay.setVisibility(View.VISIBLE);
                    }
                })
                .build();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v == fabNextMonth) {
            curMonth++;
            if (curMonth == 12) {
                curMonth = 0;
                curYear++;
            }
            curMonthS = String.format("%02d", curMonth + 1);
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurEarningMonth.setText(DateToDisplay);
            sumEarn = "₪" + eoh.getSumEarningByDate(curMonthS, curYearS);
            sumPay = "₪" + eoh.getSumPaymentByDate(curMonthS, curYearS);
            sumTips = "₪" + eoh.getSumTipsByDate(curMonthS, curYearS);
            hours = eoh.calculateHoursbyDate(curMonthS, curYearS);
            minutes = eoh.calculateMinutesbyDate(curMonthS, curYearS);
            monthlyHours = String.format("%02d:%02d", hours, minutes);
            tvMonthlyEarnings.setText(" " + sumEarn);
            tvMonthlyHours.setText(monthlyHours + "שעות");
            SharedPreferences earnInfo = getActivity().getSharedPreferences("earnInfo", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor earnInfoEdit = earnInfo.edit();
            earnInfoEdit.putString("month", curMonthS);
            earnInfoEdit.putInt("year", curYear);
            earnInfoEdit.apply();
        }
        if (v == fabPrevMonth) {
            curMonth--;
            if (curMonth == -1) {
                curMonth = 11;
                curYear--;
            }
            curMonthS = String.format("%02d", curMonth + 1);
            curYearS = Integer.toString(curYear);
            curMonthDisplay = Months[curMonth];
            DateToDisplay = curMonthDisplay + " " + curYearS;
            tvCurEarningMonth.setText(DateToDisplay);
            sumEarn = "₪" + eoh.getSumEarningByDate(curMonthS, curYearS);
            sumPay = "₪" + eoh.getSumPaymentByDate(curMonthS, curYearS);
            sumTips = "₪" + eoh.getSumTipsByDate(curMonthS, curYearS);
            hours = eoh.calculateHoursbyDate(curMonthS, curYearS);
            minutes = eoh.calculateMinutesbyDate(curMonthS, curYearS);
            monthlyHours = String.format("%02d:%02d", hours, minutes);
            tvMonthlyEarnings.setText(" " + sumEarn);
            tvMonthlyHours.setText(monthlyHours + "שעות");
            SharedPreferences earnInfo = getActivity().getSharedPreferences("earnInfo", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor earnInfoEdit = earnInfo.edit();
            earnInfoEdit.putString("month", curMonthS);
            earnInfoEdit.putInt("year", curYear);
            earnInfoEdit.apply();
        }
    }
    public void sendIdtoUpdate(long id) {
        Bundle idB = new Bundle();
        idB.putLong("id", id);

        UpdateEarningFragment uef = new UpdateEarningFragment();
        uef.setArguments(idB);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.AppFragmentContainer, uef)
                .addToBackStack(null)
                .commit();
    }

    public void showBottomDialog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetStyle);
        View sheetView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_earning_desc, (LinearLayout) getView().findViewById(R.id.dialogContainer));
        SharedPreferences info = getActivity().getSharedPreferences("earnInfo", getActivity().MODE_PRIVATE);
        tvHoursNum = sheetView.findViewById(R.id.tvHoursNum);
        tvPaymentNum = sheetView.findViewById(R.id.tvPaymentNum);
        tvTipsNum = sheetView.findViewById(R.id.tvTipsNum);
        tvEarningNum = sheetView.findViewById(R.id.tvEarningNum);
        curYearS = Integer.toString(curYear);
        hours = eoh.calculateHoursbyDate(curMonthS, curYearS);
        minutes = eoh.calculateMinutesbyDate(curMonthS, curYearS);
        monthlyHours = String.format("%02d:%02d", hours, minutes);
        earning = Double.toString(eoh.getSumEarningByDate(curMonthS, curYearS));
        payment = Double.toString(eoh.getSumPaymentByDate(curMonthS, curYearS));
        tips = Double.toString(eoh.getSumTipsByDate(curMonthS, curYearS));
        tvHoursNum.setText(monthlyHours);
        tvPaymentNum.setText("₪" + payment);
        tvTipsNum.setText("₪" + tips);
        tvEarningNum.setText("₪" + earning);
        sheetView.findViewById(R.id.ivCloseDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}
