package com.example.walletwise.Earnings.EarnOpenHelper;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walletwise.R;

public class EarningViewHolder extends RecyclerView.ViewHolder {
    TextView startDate,startTime,endDate, endTime , money;

    public EarningViewHolder( View itemView) {
        super(itemView);
        startDate=itemView.findViewById(R.id.etStartDate1);
        startTime=itemView.findViewById(R.id.etStartTime1);
        endDate=itemView.findViewById(R.id.etEndDate1);
        endTime=itemView.findViewById(R.id.etEndTime1);
        money=itemView.findViewById(R.id.etMoney1);


    }
}
