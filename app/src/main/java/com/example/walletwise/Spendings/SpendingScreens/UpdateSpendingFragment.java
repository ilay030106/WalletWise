package com.example.walletwise.Spendings.SpendingScreens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.walletwise.R;
import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;
import com.example.walletwise.UserInfoAndHomeScreen.AppScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class UpdateSpendingFragment extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener {

     long id = 0;
     EditText etDesc, etPrice;
     MaterialButton btnUpdate, btnDate, btnTime, btnDelete, btnPhotoU;
     String time1 = "";
     String date1 = "";
     String spendType = "";
     MaterialAutoCompleteTextView etTypeU;
     String[] SpendTypes;
     SpendingsOpenHelper soh;
     MaterialTimePicker timePicker;
     MaterialDatePicker datePicker;
     FloatingActionButton fabCloseU,fabShowPic;
     ImageView imgPicU;
    ArrayAdapter<String> adapter;
    boolean hasPic;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    int monthly=0;
    byte[] blob;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_spending, container, false);
        soh = new SpendingsOpenHelper(requireContext());

        etDesc = view.findViewById(R.id.etDescU);
        etPrice = view.findViewById(R.id.etPriceU);
        etTypeU = view.findViewById(R.id.etTypeDropMenuU);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDate = view.findViewById(R.id.btnDateU);
        btnTime = view.findViewById(R.id.btnTimeU);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnPhotoU = view.findViewById(R.id.btnPhotoU);
        fabCloseU = view.findViewById(R.id.fabCloseU);
        fabShowPic = view.findViewById(R.id.fabShowPicU);
        btnUpdate.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        fabCloseU.setOnClickListener(this);
        fabShowPic.setOnClickListener(this);
        btnPhotoU.setOnClickListener(this);

        SpendTypes = getResources().getStringArray(R.array.types);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.type_dropdown_item, SpendTypes);
        etTypeU.setAdapter(adapter);
        etTypeU.setOnItemClickListener((adapterView, view1, i, l) ->
                spendType = adapterView.getItemAtPosition(i).toString());
        soh = new SpendingsOpenHelper(getActivity());
        monthly=0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getLong("id");
        }
        if (id != 0) {
            soh.open();
            Spending c = soh.getSpendingbyID(id);
            soh.close();
            etDesc.setText(c.getDesc());
            etPrice.setText(Double.toString(c.getPrice()));
            etTypeU.setText(c.getType());
            btnDate.setText(c.getDate());
            btnTime.setText(c.getTime());
            blob = c.getPic();
            hasPic=true;
        }
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

        datePicker = MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
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
                int monthOfYear = systemCalender.get(Calendar.MONTH) + 1;
                int dayOfMonth = systemCalender.get(Calendar.DAY_OF_MONTH);
                date1 = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear, year);
                btnDate.setText(date1);
                datePicker.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==btnTime){
            timePicker.show(getChildFragmentManager(),"timePicker");
        }
        if(v==btnDate){
            datePicker.show(getChildFragmentManager(),"datePicker");
        }
        if(v==btnUpdate){
            String desc=etDesc.getText().toString();
            double priceDouble=Double.parseDouble(etPrice.getText().toString());
            String date1=btnDate.getText().toString();
            String time1=btnTime.getText().toString();
            String type1 = etTypeU.getText().toString();
            byte[] img= imageViewToByte(imgPicU);
            int monthlyExp=monthly;
            if(date1.equals("")){
                Toast.makeText(getActivity(),"אתה צריך לבחור תאריך בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(time1.equals("")){
                Toast.makeText(getActivity(),"אתה צריך לבחור שעה בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(time1.equals("")&&date1.equals("")){
                Toast.makeText(getActivity(),"אתה צריך לבחור תאריך ושעה בשביל לשמור הוצאה",Toast.LENGTH_LONG).show();
            }
            else if(priceDouble<=0){
                Toast.makeText(getActivity(),"המחיר של ההוצאה צריך להיות מספר חיובי ",Toast.LENGTH_LONG).show();

            }
            if(type1.equals("")){
                Toast.makeText(getActivity(),"אתה חייב לבחור סוג להוצאה ",Toast.LENGTH_LONG).show();

            }
            else{
                Spending s = new Spending(desc,priceDouble,type1,date1,time1,img);
                s.setId(id);
                soh.open();
                soh.updateSpending(s);
                soh.close();
                Toast.makeText(getActivity(),"ההוצאה עודכנה! ",Toast.LENGTH_LONG).show();
                AppScreen appScreen = (AppScreen) getActivity();
                appScreen.replaceFragment(new SpendingListFragment());


            }
        }
        if(v==btnPhotoU){
            showPictureDialog();
        }
        if(v==btnDelete){
            soh.open();
            soh.deleteById(id);
            soh.close();
            Toast.makeText(getActivity(),"ההוצאה נמחקה! ",Toast.LENGTH_LONG).show();
            AppScreen appScreen = (AppScreen) getActivity();
            appScreen.replaceFragment(new SpendingListFragment());
        }
        if(v==fabCloseU){
            AppScreen appScreen = (AppScreen) getActivity();
            appScreen.replaceFragment(new SpendingListFragment());        }
        if(v==fabShowPic){
            if(hasPic) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.pic_dialog);
                soh.open();
                Spending c = soh.getSpendingbyID(id);
                soh.close();
                Bitmap bitmap =BitmapFactory.decodeByteArray(blob, 0, blob.length);
                imgPicU = dialog.findViewById(R.id.imgPic);
                imgPicU.setImageBitmap(bitmap);
                dialog.show();
            }
            else{
                Toast.makeText(getActivity(),"תמונה אינה הוזנה כראוי",Toast.LENGTH_LONG);
            }


        }

    }


    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        spendType = SpendTypes[newVal];
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    String path = saveImage(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    bitmap=bitmap1;
                    imgPicU.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgPicU.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            blob = imageViewToByte(imgPicU);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }}
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        return "";
    }




    //Convert imageView to byte[]
    private byte[] imageViewToByte(ImageView image) {
        if (image == null) {
            return blob;
        }
        Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[]byteArray=stream.toByteArray();
        return byteArray;
    }



}
