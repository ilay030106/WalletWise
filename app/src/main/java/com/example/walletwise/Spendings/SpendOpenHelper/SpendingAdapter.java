package com.example.walletwise.Spendings.SpendOpenHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletwise.R;

import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingViewHolder> {
    Context context;
    List<Spending> objects;





    public SpendingAdapter(Context context, List<Spending> objects) {
        this.objects = objects;
        this.context = context;
    }

    @Override
    public SpendingViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.one_row_spend, null);
        return new SpendingViewHolder(view);
    }




    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        Spending temp = objects.get(position);
        holder.desc.setText(temp.getDesc());
        holder.price.setText("₪" + temp.getPrice());
        holder.type.setText(temp.getType());
        holder.date.setText(temp.getDate());
        holder.time.setText(temp.getTime());
        byte[] image = temp.getPic();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.pic.setImageBitmap(bitmap);



    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
    public long getSpendingId(int pos){
        return objects.get(pos).getId();
    }



}



