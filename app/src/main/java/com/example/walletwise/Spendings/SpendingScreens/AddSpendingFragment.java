package com.example.walletwise.Spendings.SpendingScreens;

import static java.lang.Double.parseDouble;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.walletwise.R;
import com.example.walletwise.Spendings.NotFinishedService;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.UserInfoAndHomeScreen.AppScreen;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSpendingFragment extends Fragment implements View.OnClickListener {
    TextInputEditText etDesc, etPrice;
    MaterialTimePicker timePicker;
    MaterialDatePicker datePicker;
    Button btnAddSpend, btnDate, btnTime, btnPhoto;

    String time1 = "", date1 = "", spendType = "",desc="",type1="",dayOfMonth =" ",monthAndYear = " ";

    FloatingActionButton fabClose,fabShowPic;

    SpendingsOpenHelper soh;
    String[] types;
    ImageView imgPic;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    MaterialAutoCompleteTextView etType;
    ArrayAdapter<String> adapter;
    boolean hasPic=false;
    Bitmap bitmap1;
    double priceDouble=0;
    byte []img;

    boolean finished;
    String[] Months;



    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_spending, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finished=false;
        Months = getResources().getStringArray(R.array.months);

        etDesc = view.findViewById(R.id.etDesc);
        etPrice = view.findViewById(R.id.etPrice);
        btnAddSpend = view.findViewById(R.id.btnAddSpend);
        btnDate = view.findViewById(R.id.btnDate);
        btnTime = view.findViewById(R.id.btnTime);
        btnPhoto = view.findViewById(R.id.btnPhoto);
        fabClose = view.findViewById(R.id.fabClose);
        fabShowPic = view.findViewById(R.id.fabShowPic);
        imgPic = view.findViewById(R.id.imgPic);
        etType = view.findViewById(R.id.etTypeDropMenu);
        btnAddSpend.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        fabClose.setOnClickListener(this);
        fabShowPic.setOnClickListener(this);
        types = getResources().getStringArray(R.array.types);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.type_dropdown_item, types);
        etType.setAdapter(adapter);
        etType.setOnItemClickListener((adapterView, view1, i, l) ->
                spendType = adapterView.getItemAtPosition(i).toString());

        soh = new SpendingsOpenHelper(getActivity());
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("בחר את שעת ההוצאה")
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText("הוסף")
                .setNegativeButtonText("בטל")
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minute = timePicker.getMinute();
                int hour = timePicker.getHour();
                time1 = String.format("%02d:%02d", hour, minute);
                btnTime.setText(time1);
                timePicker.dismiss();
            }
        });

        datePicker = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("בחר את תאריך ההוצאה")
                .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                Calendar systemCalender = Calendar.getInstance();
                systemCalender.setTimeInMillis(selection);
                int year = systemCalender.get(Calendar.YEAR);
                int monthOfYear = systemCalender.get(Calendar.MONTH);
                int dayOfMonth1 = systemCalender.get(Calendar.DAY_OF_MONTH);
                dayOfMonth=Integer.toString(dayOfMonth1);
                monthAndYear = year  + " " + Months[monthOfYear];
                date1 = String.format("%02d/%02d/%d", dayOfMonth1, monthOfYear+1, year);
                btnDate.setText(date1);
                datePicker.dismiss();
            }


        });
        desc = etDesc.getText().toString();
        priceDouble = 0;
        date1 = btnDate.getText().toString();
        time1 = btnTime.getText().toString();
        type1 = etType.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (v == fabClose) {
            if (getActivity() instanceof AppScreen) {
                AppScreen appScreen = (AppScreen) getActivity();
                appScreen.replaceFragment(new SpendingListFragment());
            }
        }
        if (v == btnPhoto) {
            showPictureDialog();
        }
        if (v == btnTime) {
            timePicker.show(getChildFragmentManager(), "timePicker");
        }
        if (v == btnDate) {
            datePicker.show(getChildFragmentManager(), "datePicker");

        }
        if (v == btnAddSpend) {
             desc = etDesc.getText().toString();
             priceDouble = parseDouble(etPrice.getText().toString());
             date1 = btnDate.getText().toString();
             time1 = btnTime.getText().toString();
             type1 = etType.getText().toString();
             img = imageViewToByte(imgPic);

            if (date1.equals("")) {
                Toast.makeText(getActivity(), "אתה צריך לבחור תאריך בשביל לשמור הוצאה", Toast.LENGTH_LONG).show();
            } else if (time1.equals("")) {
                Toast.makeText(getActivity(), "אתה צריך לבחור שעה בשביל לשמור הוצאה", Toast.LENGTH_LONG).show();
            } else if (time1.equals("") && date1.equals("")) {
                Toast.makeText(getActivity(), "אתה צריך לבחור תאריך ושעה בשביל לשמור הוצאה", Toast.LENGTH_LONG).show();
            }

            else if (priceDouble <= 0) {
                etDesc.setError("המחיר צריך להיות גדול מ0");
            } else {
                soh.open();
                Spending spend = new Spending(desc, priceDouble, type1, date1, time1, img);
                soh.createSpending(spend);
                soh.close();
                finished=true;
                Toast.makeText(getActivity(), "ההוצאה נשמרה בהצלחה! ", Toast.LENGTH_LONG).show();




                if (getActivity() instanceof AppScreen) {
                    AppScreen appScreen = (AppScreen) getActivity();
                    appScreen.replaceFragment(new SpendingListFragment());
                }
            }
        }
        if(v==fabShowPic){
            if(bitmap1!=null) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.pic_dialog);
                imgPic = dialog.findViewById(R.id.imgPic);
                imgPic.setImageBitmap(bitmap1);
                dialog.show();
            }
            else{
                Toast.makeText(getActivity(),"לא הוזנה תמונה עדיין ",Toast.LENGTH_LONG);
            }
        }
    }






    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("נא לבחור מאיפה להוסיף תמונה:");
        String[] pictureDialogItems = {
                "מהגלריה",
                "מהמצלמה"};
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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    bitmap1=bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgPic.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        return "";
    }


    //Convert imageView to byte[]
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!finished){
            //לעשות סרוויס
        }



        }


}




