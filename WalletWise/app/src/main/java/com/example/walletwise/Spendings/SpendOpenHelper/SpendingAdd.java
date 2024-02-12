package com.example.walletwise.Spendings.SpendOpenHelper;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.Spendings.SpendingScreens.SpendingScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@RequiresApi(api = Build.VERSION_CODES.O)
public class SpendingAdd extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    TextInputEditText etDesc, etPrice;
    TextView tvDate,tvTime;
    MaterialTimePicker timePicker;//M3 Google library
    MaterialDatePicker datePicker;//M3 Google library
    MaterialButton btnAddSpend, btnDate, btnTime,btnPhoto;


    String time1="",date1="",spendType="";

    FloatingActionButton fabClose;

    SpendingsOpenHelper soh;
    String[] types;
    NumberPicker npTypes;
    ImageView imgPic;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;




    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_add);
        etDesc = findViewById(R.id.etDesc);
        etPrice = findViewById(R.id.etPrice);
        btnAddSpend = findViewById(R.id.btnAddSpend);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnPhoto= findViewById(R.id.btnPhoto);
        fabClose = findViewById(R.id.fabClose);
        tvDate=findViewById(R.id.tvDate);
        tvTime=findViewById(R.id.tvTime);
        npTypes=findViewById(R.id.npTypes);
        imgPic=findViewById(R.id.imgPic);
        types=getResources().getStringArray(R.array.types);
        btnAddSpend.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        fabClose.setOnClickListener(this);
        npTypes.setMinValue(0);
        npTypes.setMaxValue(7);
        npTypes.setValue(0);
        npTypes.setOnValueChangedListener(this);
        npTypes.setDisplayedValues(types);
        soh = new SpendingsOpenHelper(this);
        npTypes.setTextColor(getColor(R.color.white));
        etDesc.setHintTextColor(getResources().getColor(R.color.lightGrey));
        etPrice.setHintTextColor(getResources().getColor(R.color.lightGrey));



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
        datePicker= MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("בחר את תאריך ההוצאה")
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

        if (v == btnAddSpend) {
            String desc=etDesc.getText().toString();
            double priceDouble=parseDouble(etPrice.getText().toString());
            String date1=btnDate.getText().toString();
            String time1=btnTime.getText().toString();
            String type1=spendType;
            byte[] img =imageViewToByte(imgPic);

            if(date1.equals("")){
                Toast.makeText(this,"אתה צריך לבחור תאריך בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(time1.equals("")){
                Toast.makeText(this,"אתה צריך לבחור שעה בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(time1.equals("")&&date1.equals("")){
                Toast.makeText(this,"אתה צריך לבחור תאריך ושעה בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(spendType.equals("")){
                Toast.makeText(this,"אתה צריך לבחור סוג להוצאה כדי לשמור אותה",Toast.LENGTH_LONG).show();

            }
            else if(priceDouble<=0){
                etDesc.setError("המחיר צריך להיות גדול מ0");

            }
            else{
                soh.open();
                Spending spend = new Spending(desc,priceDouble,type1,date1,time1,img);
                soh.createSpending(spend);
                soh.close();
                Toast.makeText(this,"ההוצאה נשמרה בהצלחה! ",Toast.LENGTH_LONG).show();
                Intent i= new Intent(this, SpendingScreen.class);
                startActivity(i);
            }



        }
        if(v==btnTime){
            timePicker.show(this.getSupportFragmentManager(),"timePicker");
        }
        if(v==btnDate){
            datePicker.show(this.getSupportFragmentManager(),"datePicker");
        }

        if(v==fabClose){
            startActivity(new Intent(this, SpendingScreen.class));
        }
        if(v==btnPhoto){
            showPictureDialog();

        }

    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        spendType=types[newVal];
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
                    Toast.makeText(SpendingAdd.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imgPic.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SpendingAdd.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgPic.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(SpendingAdd.this, "Image Saved!", Toast.LENGTH_SHORT).show();
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