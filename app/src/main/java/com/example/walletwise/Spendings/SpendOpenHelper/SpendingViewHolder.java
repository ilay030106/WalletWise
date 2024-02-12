package com.example.walletwise.Spendings.SpendOpenHelper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletwise.R;

class SpendingViewHolder extends RecyclerView.ViewHolder {



    TextView desc, price, type,date,time;

    ImageView pic;
    CardView SpendCard;



    public SpendingViewHolder( View itemView) {
        super(itemView);
        desc=itemView.findViewById(R.id.etDesc1);
        price=itemView.findViewById(R.id.etPrice1);
        type=itemView.findViewById(R.id.etType1);
        date=itemView.findViewById(R.id.etDate1);
        time=itemView.findViewById(R.id.etTime1);
        pic=itemView.findViewById(R.id.ivItemPic);
        SpendCard=itemView.findViewById(R.id.SpendCard);

    }
}
