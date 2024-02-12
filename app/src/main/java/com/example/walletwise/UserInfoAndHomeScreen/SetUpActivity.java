package com.example.walletwise.UserInfoAndHomeScreen;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walletwise.R;

public class SetUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etWage;
    Button btnLogIn;

    private static final String PREFS_NAME = "setUpComplete";
    private static final String KEY_ACTIVITY_SHOWN = "activityShown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        etName = findViewById(R.id.etName);
        etWage = findViewById(R.id.etWage);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);
        etName.setHintTextColor(getResources().getColor(R.color.white));
        etWage.setHintTextColor(getResources().getColor(R.color.white));




    }

    @Override
    public void onClick(View v) {
        if(v==btnLogIn){
            String wage = etWage.getText().toString();
            double checkWage= parseDouble(wage);
            String name =etName.getText().toString();

            for(int i = 0 ; i <name.length();i++){
                if(!((name.charAt(i)>='a' && name.charAt(i)<='z')||(name.charAt(i)>='A' && name.charAt(i)<='Z')||(name.charAt(i)>='א' && name.charAt(i)<='ת')))
                {
                    Toast.makeText(SetUpActivity.this,"השם חייב להכיל רק אותיות "+ "\n",Toast.LENGTH_SHORT).show();
                }

            }
            /*if(name.length()>2){
                Toast.makeText(SetUpActivity.this,"אורך השם צריך להיות מעל 2 תווים " + "\n",Toast.LENGTH_SHORT).show();
            }*/
            if(checkWage<=0){
                Toast.makeText(SetUpActivity.this,"גודל השכר צריך להיות יותר גדול מ0 "+ "\n",Toast.LENGTH_SHORT).show();

            }

            else{
                saveNameAndWage();
                Intent i=new Intent(this, AppScreen.class);
                startActivity(i);
            }

        }



    }
    public void saveNameAndWage(){
        SharedPreferences sharedPref= getSharedPreferences("setUp",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPref.edit();
        edit.putString("name",etName.getText().toString());
        edit.putString("wage",etWage.getText().toString());
        edit.apply();

    }


    }
