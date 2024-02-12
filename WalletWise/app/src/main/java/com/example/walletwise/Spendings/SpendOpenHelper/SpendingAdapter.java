package com.example.walletwise.Spendings.SpendOpenHelper;

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

import com.example.walletwise.R;

import java.util.List;

public class SpendingAdapter extends ArrayAdapter<Spending> {
    Context context;
    List<Spending> objects;

    public SpendingAdapter(Context context, int resource, List<Spending> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.one_row_spend, parent, false);
        TextView desc =  view.findViewById(R.id.etDesc1);
        TextView price =  view.findViewById(R.id.etPrice1);
        TextView type = view.findViewById(R.id.etType1);
        TextView date = view.findViewById(R.id.etDate1);
        TextView time = view.findViewById(R.id.etTime1);
        ImageView pic=view.findViewById(R.id.ivItemPic);

        Spending temp = objects.get(position);
        desc.setText(temp.getDesc());
        price.setText("₪"+temp.getPrice());
        type.setText(temp.getType());
        date.setText(temp.getDate());
        time.setText(temp.getTime());
        byte[]image=temp.getPic();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        pic.setImageBitmap(bitmap);



        return view;
    }

}
