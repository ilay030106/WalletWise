package com.example.walletwise.Earnings.EarnOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walletwise.Earnings.EarnOpenHelper.Earning;
import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;

import java.util.List;

public class EarningAdapter extends ArrayAdapter<Earning> {
    Context context;
    List<Earning> objects;
    public EarningAdapter(Context context, int resource, List<Earning> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.one_row_earn, parent, false);
        TextView startDate =  view.findViewById(R.id.etStartDate1);
        TextView startTime =  view.findViewById(R.id.etStartTime1);
        TextView endDate = view.findViewById(R.id.etEndDate1);
        TextView endTime = view.findViewById(R.id.etEndTime1);
        TextView money = view.findViewById(R.id.etMoney1);

        Earning temp = objects.get(position);
        startDate.setText(temp.getStartDate());
        startTime.setText(temp.getStartTime());
        endDate.setText(temp.getEndDate());
        endTime.setText(temp.getEndTime());
        if(temp.getTip()>0)
            money.setText(" ₪"+temp.getMoney()+" + ₪"+temp.getTip()+ " טיפ");
        else
            money.setText("₪"+temp.getMoney());

        return view;
    }
}
