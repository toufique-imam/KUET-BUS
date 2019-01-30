package com.example.kuetbus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

class bus_data{
    boolean from_campus;
    String time1, loc1, time2, msg, type, loc2;
    bus_data() { }
}



public class fromCampus extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String get_next_day(String today){
       if(today.equals("FRIDAY")){
           return "SATURDAY";
       }
       else return "SUNDAY";
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(fromCampus.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    //System.out.println(new SimpleDateFormat("EE",Locale.ENGLISH).format(date.getTime()));
    //String day=LocalDate.now().getDayOfWeek().name();
    String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    String next_day=get_next_day(day);
    String day_now=day;
    final String API = "https://script.googleusercontent.com/macros/echo?user_content_key=57IfaKb-U8YMtPCyYb3KhpRBUh4wnT0IqN6AgG-TQz6G-woCggjjiRMSX8Iso9CVcnxvEwLk4OopX9Bv_-8VhNCZxplE1j5qm5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnA4pYc6ryj8COOkQV7W5DxR0VUwzhItFQede_pq8mT2-DhK9GunvXrgK0fRfo0-HI_wSrHcae_pi&lib=MeRHtBj1FCHOrk7QgO_aaFlqR9hX156uw";
    Vector<bus_data> arrayList1, arrayList2,vector;
    TextFileHandler textFileHandler;
    String json_str;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Date get_time(String timex){
        DateFormat format=new SimpleDateFormat("hh:mm a",Locale.US);
        try {
            return  format.parse(timex);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(fromCampus.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initialize();
        Log.e("DAYNAME",day);
    }
    void set_view(int idx,boolean mark){
        Adapter_1 adapter_1;
        if(idx==0) adapter_1=new Adapter_1(fromCampus.this,arrayList1,mark);
        else if(idx==2)adapter_1=new Adapter_1(fromCampus.this,vector,mark);
        else adapter_1=new Adapter_1(fromCampus.this,arrayList2,mark);
        linearLayoutManager=new LinearLayoutManager(fromCampus.this,RecyclerView.VERTICAL,false);
        //GridLayoutManager giid=new GridLayoutManager(fromCampus.this,2,RecyclerView.VERTICAL,false);
        recyclerView.setAdapter(adapter_1);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void initialize() {
        arrayList1 = new Vector<>();
        arrayList2 = new Vector<>();
        vector=new Vector<>();
        textFileHandler = new TextFileHandler();
        Pair<Boolean,String>json=textFileHandler.READ_TEXT("Test.txt");
        recyclerView=findViewById(R.id.recycler_view_main);
        if(json.first==Boolean.TRUE){
            json_str=json.second;
            update(day_now);
        }
        else{
            update_json_data();
        }
        //log_json_arra();
        set_view(2,true);
        Toast.makeText(fromCampus.this,"Double Tap on Item to see Notes",Toast.LENGTH_LONG).show();
    }
    void log_json_arra(){
        for(int i=0;i<arrayList1.size();i++){
            log_bus(arrayList1.get(i));
        }
    }
    void update(String day){
        try {
            JSONObject jsonObject = new JSONObject(json_str);
            JSONObject jsonObjectValues;
            try {
                jsonObjectValues = jsonObject.getJSONObject("values");
                try {

                    JSONArray morning = jsonObjectValues.getJSONArray("Morning");
                    JSONArray noon = jsonObjectValues.getJSONArray("Noon");
                    JSONArray afternoon = jsonObjectValues.getJSONArray("Afternoon");
                    JSONArray night = jsonObjectValues.getJSONArray("Night");
                    JSONArray saturday = jsonObjectValues.getJSONArray("Saturday");
                    Log.e("DAYYY",day);
                    if(!day.equals("FRIDAY")) {
                        if (!day.equals("SATURDAY")) {
                            process_data(morning);
                            process_data(noon);
                            process_data(afternoon);
                            process_data(night);
                        } else process_data(saturday);
                    }
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                }
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
            }
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
        }
    }
    String process_note_for_location(String s){
        //StringBuilder ans=new StringBuilder();
        if(s.contains("Ferighat") || s.contains("ferighat"))return "Ferighat";
        if(s.contains("Rupsha") || s.contains("rupsha"))return "Rupsha";
        if(s.contains("Moylapota"))return "Moylapota";
        if(s.contains("Dakbangla"))return  "Dakbangla";
        if(s.contains("Sonadanga"))return "Sonadanga";
        if(s.contains("Gollamari"))return "Gollamari";
        return "Dakbangla";
        //else return  ans.toString();
    }
    private void update_json_data(){
        GetBusData getBusData=new GetBusData();
        getBusData.execute();
    }
    private class GetBusData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(fromCampus.this);
            progressDialog.setMessage("Updating");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsnStr = sh.makeServiceCall(API);
           try{ Log.e("JSON", jsnStr);}
           catch (Exception e){
               Log.e("JSONERROR",e.getMessage());
           }
            if (jsnStr != null) {
                textFileHandler.WRITE_TEXT("Test.txt", jsnStr);
            }
            return  null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            json_str=textFileHandler.READ_TEXT("Test.txt").second;
            Toast.makeText(fromCampus.this,"DONE",Toast.LENGTH_SHORT).show();
            Log.e("DAYY",day_now);
            update(day);
            set_view(2,true);
        }
    }

    String process_time(String a) {
        int i;
        for (i = 0; i < a.length(); i++) {
            if (Character.isDigit(a.charAt(i))) {
                break;
            }
        }
        return a.substring(i);
    }
    String process_location(String a) {
        int i;
        for (i = 0; i < a.length(); i++) {
            if (!Character.isAlphabetic(a.charAt(i)))break;
        }
        Log.e("OKKK", a.substring(0, i));
        Log.e("OKKK", String.valueOf(i));
        return a.substring(0, i);
    }
    void log_bus(bus_data a){
        Log.e("WHAT",a.loc1);
        Log.e("WHAT",a.loc2);
        Log.e("WHAT",a.time1);
        Log.e("WHAT",a.time2);
        Log.e("WHAT",a.msg);
        Log.e("WHAT",a.type);
    }
    void process_data(JSONArray jsonArray) throws JSONException {
        for (int i = 1; i < jsonArray.length(); i++) {
            bus_data bsd = new bus_data();
            bus_data bsd1 = new bus_data();
            JSONObject tmp = null;
            tmp = jsonArray.getJSONObject(i);
            bsd.type = tmp.getString("index0");
            bsd1.type = tmp.getString("index0");
            bsd.loc1 = "KUET";
            bsd1.loc1 = "KUET";
            bsd.time1 = tmp.getString("index1");
            bsd1.time1 = tmp.getString("index1");
            String xd = tmp.getString("index2");
            bsd.loc2 = process_location(xd);
            bsd1.loc2 = process_location(xd);
            bsd.time2 = process_time(xd);
            bsd1.time2 = process_time(xd);
            bsd.msg = tmp.getString("index3");
            bsd1.msg = tmp.getString("index3");
            if(bsd.loc2.isEmpty()){
                xd=process_note_for_location(bsd.msg);
                bsd.loc2=xd;
                bsd1.loc2=xd;
            }
            bsd.from_campus=true;
            bsd1.from_campus=false;
            arrayList1.add(bsd);
            arrayList2.add(bsd1);
            vector.add(bsd);
            vector.add(bsd1);
        }
        Collections.sort(arrayList1, new Comparator<bus_data>() {
            @Override
            public int compare(bus_data o1, bus_data o2) {
                Date tim1,tim2;
                if(o1.from_campus){
                    tim1=get_time(o1.time1);
                }else
                    tim1=get_time(o1.time2);
                if(o2.from_campus){
                    tim2=get_time(o2.time1);
                }else
                    tim2=get_time(o2.time2);
                return tim1.compareTo(tim2);
            }
        });   Collections.sort(arrayList2, new Comparator<bus_data>() {
            @Override
            public int compare(bus_data o1, bus_data o2) {
                Date tim1,tim2;
                if(o1.from_campus){
                    tim1=get_time(o1.time1);
                }else
                    tim1=get_time(o1.time2);
                if(o2.from_campus){
                    tim2=get_time(o2.time1);
                }else
                    tim2=get_time(o2.time2);
                return tim1.compareTo(tim2);
            }
        });   Collections.sort(vector, new Comparator<bus_data>() {
            @Override
            public int compare(bus_data o1, bus_data o2) {
                Date tim1,tim2;
                if(o1.from_campus){
                    tim1=get_time(o1.time1);
                }else
                    tim1=get_time(o1.time2);
                if(o2.from_campus){
                    tim2=get_time(o2.time1);
                }else
                    tim2=get_time(o2.time2);
                return tim1.compareTo(tim2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    /*    if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.drawer_menu_for_you) {
            Toast.makeText(fromCampus.this,"Not Yet Implemented",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.drawer_menu_upcoming) {
            if(day_now!=day){update(day);day_now=day;}
            set_view(2,true);
            //Toast.makeText(fromCampus.this,"Not Yet Implemented",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.drawer_menu_full_list) {
            if(day_now!=day){update(day);day_now=day;}
            set_view(0,false);
        } else if (id == R.id.drawer_menu_To_KUET) {
            if(day_now!=day){update(day);day_now=day;}
            set_view(1,true);
        } else if (id == R.id.drawer_menu_from_KUET) {
            if(day_now!=day){update(day);day_now=day;}
            set_view(0,true);
        } else if (id == R.id.drawer_menu_update) {
            update_json_data();
        }
        else if(id==R.id.drawer_menu_next_full_list){
            day_now=next_day;
            update(next_day);
            set_view(0,false);
        }
        else if(id==R.id.drawer_menu_github){
            String url="https://github.com/sabertooth9/KUET-BUS.git";
            Uri webpage = Uri.parse(url);
            Intent intn = new Intent(Intent.ACTION_VIEW, webpage);
            if (intn.resolveActivity(getPackageManager()) != null) {
                startActivity(intn);
            }
        }
        else if(id==R.id.drawer_menu_feedback){
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "nuhash1083s@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            Email.putExtra(Intent.EXTRA_TEXT, "Hello Developer Kuet Bus's...," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
