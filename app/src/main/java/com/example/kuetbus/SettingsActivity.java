package com.example.kuetbus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputEditText;

import static com.example.kuetbus.fromCampus.colordark;
import static com.example.kuetbus.fromCampus.darkOn;

public class SettingsActivity extends AppCompatActivity {
    TextInputEditText darkmode,update;
    Switch aSwitch;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        Intent intent=new Intent();
        intent.putExtra("OK",1);
        setResult(Activity.RESULT_OK,intent);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    darkOn=true;
                    linearLayout.setBackgroundColor(colordark);
                    darkmode.setTextColor(Color.WHITE);
                    update.setTextColor(Color.WHITE);
                }
                else{
                    darkOn=false;
                    linearLayout.setBackgroundColor(Color.WHITE);
                    darkmode.setTextColor(colordark);
                    update.setTextColor(colordark);
                }
            }
        });
    }
    void init(){
        darkmode=findViewById(R.id.text_view_setting_dark);
        update=findViewById(R.id.text_view_setting_update);
        aSwitch=findViewById(R.id.switch_setting_dark);
        linearLayout=findViewById(R.id.linear_layout_setting);
        aSwitch.setChecked(darkOn);
        if(darkOn){
            linearLayout.setBackgroundColor(colordark);
            darkmode.setTextColor(Color.WHITE);
            update.setTextColor(Color.WHITE);
        }
        else{
            linearLayout.setBackgroundColor(Color.WHITE);
            darkmode.setTextColor(colordark);
            update.setTextColor(colordark);
        }
    }
}
