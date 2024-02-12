package com.example.walletwise.Spendings.SpendingScreens;

import static java.lang.Integer.parseInt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingAdd;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.Spendings.SpendingScreens.SpendingScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

public class UpdateSpendingActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    long id=0;
    EditText etDesc, etPrice;
    TextView tvDate,tvTime;
    NumberPicker npTypesU;
    MaterialButton btnUpdate, btnDate, btnTime,btnDelete,btnPhotoU;
    String time1="";
    String date1="";
    String spendType="";

    String[] SpendTypes;
    SpendingsOpenHelper soh;
    MaterialTimePicker timePicker;//M3 Google library
    MaterialDatePicker datePicker;//M3 Google library
    FloatingActionButton fabCloseU;
    ImageView imgPicU;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spending);
        soh= new SpendingsOpenHelper(this);
        etDesc = findViewById(R.id.etDescU);
        etPrice = findViewById(R.id.etPriceU);
        npTypesU = findViewById(R.id.npTypesU);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDate = findViewById(R.id.btnDateU);
        btnTime = findViewById(R.id.btnTimeU);
        btnDelete=findViewById(R.id.btnDelete);
        btnPhotoU=findViewById(R.id.btnPhotoU);
        imgPicU=findViewById(R.id.imgPicU);
        fabCloseU=findViewById(R.id.fabCloseU);
        tvDate=findViewById(R.id.tvDateU);
        tvTime=findViewById(R.id.tvTimeU);
        btnUpdate.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        fabCloseU.setOnClickListener(this);
        btnPhotoU.setOnClickListener(this);
        npTypesU.setMinValue(0);
        npTypesU.setMaxValue(7);
        npTypesU.setWrapSelectorWheel(true);
        npTypesU.setOnValueChangedListener(this);
        SpendTypes=getResources().getStringArray(R.array.types);
        npTypesU.setDisplayedValues(SpendTypes);
        npTypesU.setTextColor(getColor(R.color.white));
        soh = new SpendingsOpenHelper(this);
        id = getIntent().getLongExtra("rowId", 0);
        if (id != 0) {
            soh.open();
            Spending c = soh.getSpendingbyID(id);
            soh.close();
            etDesc.setText(c.getDesc());
            etPrice.setText(Double.toString(c.getPrice()));
            npTypesU.setValue(Arrays.asList(SpendTypes).indexOf(c.getType()));
            btnDate.setText(c.getDate());
            btnTime.setText(c.getTime());
            byte[]image=c.getPic();
            Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
            imgPicU.setImageBitmap(bitmap);

        }

        timePicker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("בחר את שעת ההוצאה")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minute=timePicker.getMinute();
                int hour=timePicker.getHour();
                time1 = String.format("%02d:%02d", hour, minute);
                tvTime.setVisibility(View.VISIBLE);
                btnTime.setText(time1);
                timePicker.dismiss();
            }
        });
        datePicker= MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("בחר את תאריך ההוצאה")
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                Calendar systemCalender = Calendar.getInstance();
                systemCalender.setTimeInMillis(selection);
                int year = systemCalender.get(Calendar.YEAR);
                int monthOfYear = systemCalender.get(Calendar.MONTH)+1;
                int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
                tvDate.setVisibility(View.VISIBLE);
                date1 = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear,year);
                btnDate.setText(date1);
                datePicker.dismiss();
            }


        });
    }



    @Override
    public void onClick(View v) {
        if(v==btnTime){
            timePicker.show(this.getSupportFragmentManager(),"timePicker");
        }
        if(v==btnDate){
            datePicker.show(this.getSupportFragmentManager(),"datePicker");
        }
        if(v==btnUpdate){
            String desc=etDesc.getText().toString();
            double priceDouble=Double.parseDouble(etPrice.getText().toString());
            String date1=btnDate.getText().toString();
            String time1=btnTime.getText().toString();
            String type1=spendType;
            byte[] img= imageViewToByte(imgPicU);
            if(date1.equals("")){
                Toast.makeText(this,"אתה צריך לבחור תאריך בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
           else if(time1.equals("")){
                Toast.makeText(this,"אתה צריך לבחור שעה בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
           else if(time1.equals("")&&date1.equals("")){
                Toast.makeText(this,"אתה צריך לבחור תאריך ושעה בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(priceDouble<=0){
                Toast.makeText(this,"המחיר של ההוצאה צריך להיות מספר חיובי ",Toast.LENGTH_LONG).show();

            }
            if(type1.equals("")){
                Toast.makeText(this,"אתה חייב לבחור סוג להוצאה ",Toast.LENGTH_LONG).show();

            }
            else{
                Spending s = new Spending(desc,priceDouble,type1,date1,time1,img);
                s.setId(id);
                soh.open();
                soh.updateSpending(s);
                soh.close();
                Toast.makeText(this,"ההוצאה עודכנה! ",Toast.LENGTH_LONG).show();
                Intent i= new Intent(this, SpendingScreen.class);
                startActivity(i);

            }
        }
        if(v==btnPhotoU){
            showPictureDialog();
        }
        if(v==btnDelete){
            soh.open();
            soh.deleteById(id);
            soh.close();
            Toast.makeText(this,"ההוצאה נמחקה! ",Toast.LENGTH_LONG).show();
            Intent i= new Intent(this, SpendingScreen.class);
            startActivity(i);
        }
        if(v==fabCloseU){
            startActivity(new Intent(this, SpendingScreen.class));
        }

    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        spendType=SpendTypes[newVal];
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("נא לבחור מאיפה להוסיף תמונה:");
        String[] pictureDialogItems = {
                "מהגלריה",
                "מהמצלמה" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {

        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        startActivityForResult(intent, CAMERA);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(UpdateSpendingActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imgPicU.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateSpendingActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgPicU.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(UpdateSpendingActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }}
    public String saveImage( Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        return "";
    }




    //Convert imageView to byte[]
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[]byteArray=stream.toByteArray();
        return byteArray;
    }

}