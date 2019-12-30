package com.example.kuetbus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kuetbus.fromCampus.colordark;
import static com.example.kuetbus.fromCampus.darkOn;

public class SettingsActivity extends AppCompatActivity {
    TextInputEditText darkmode,update;
    Switch aSwitch;
    LinearLayout linearLayout;
    TextFileHandler textFileHandler;
    String json_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        Intent intent=new Intent();
        intent.putExtra("OK",1);
        textFileHandler = new TextFileHandler();
        readsettings();
        setResult(Activity.RESULT_OK,intent);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    darkOn=true;
                    updatesettings();
                    linearLayout.setBackgroundColor(colordark);
                    darkmode.setTextColor(Color.WHITE);
                    update.setTextColor(Color.WHITE);
                }
                else{
                    darkOn=false;
                    updatesettings();
                    linearLayout.setBackgroundColor(Color.WHITE);
                    darkmode.setTextColor(colordark);
                    update.setTextColor(colordark);
                }
            }
        });
    }

    void readsettings() {
        Pair<Boolean, String> json = textFileHandler.READ_TEXT("settings.txt");
        if (json.first == Boolean.TRUE) {
            json_str = json.second;
            try {
                JSONObject jsonObject = new JSONObject(json_str);
                Integer status = jsonObject.getInt("dark");
                darkOn = status == 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            updatesettings();
        }
    }

    void updatesettings() {
        if (darkOn)
            json_str = "{\"dark\":1}";
        else
            json_str = "{\"dark\":0}";
        textFileHandler.WRITE_TEXT("settings.txt", json_str);
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
