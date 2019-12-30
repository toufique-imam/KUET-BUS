package com.example.kuetbus;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;


public class fromCampus extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    String day, next_day, day_now;
    static boolean darkOn = false;
    static final int colordark = Color.rgb(43, 43, 43), colorblue = Color.rgb(13, 79, 139), whitesmoke = Color.rgb(245, 245, 245);
    final String API = "https://kuet-bus.herokuapp.com/data";
    Vector<bus_data> arrayList1, arrayList2, vector;
    TextFileHandler textFileHandler;
    String json_str;
    LinearLayoutManager linearLayoutManager;
    LinearLayout linearLayoutback;
    RecyclerView recyclerView;
    time_class timce;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    MenuItem upcoming, fromku, toku, todaysh, tomsh, upd, git, feed, stting;
    ColorStateList colorStateList1, colorStateList2;

    void darkon() {
        navigationView.setBackgroundColor(colordark);
        linearLayoutback.setBackgroundColor(colordark);
        navigationView.setItemTextColor(colorStateList1);
        navigationView.setItemIconTintList(colorStateList1);
        upcoming.setIcon(R.drawable.baseline_departure_board_white_36dp);
        fromku.setIcon(R.drawable.ic_home_white_36dp);
        toku.setIcon(R.drawable.ic_gps_not_fixed_white_36dp);
        todaysh.setIcon(R.drawable.ic_gps_fixed_white_36dp);
        tomsh.setIcon(R.drawable.ic_today_white_36dp);
        upd.setIcon(R.drawable.ic_system_update_alt_white_36dp);
        git.setIcon(R.drawable.ic_code_white_36dp);
        feed.setIcon(R.drawable.ic_near_me_white_36dp);
        stting.setIcon(R.drawable.ic_settings_applications_white_36dp);
    }

    void darkoff() {
        linearLayoutback.setBackgroundColor(Color.WHITE);
        navigationView.setBackgroundColor(Color.WHITE);
        navigationView.setItemIconTintList(colorStateList2);
        navigationView.setItemTextColor(colorStateList2);
        upcoming.setIcon(R.drawable.baseline_departure_board_black_36);
        fromku.setIcon(R.drawable.ic_home_black_36dp);
        toku.setIcon(R.drawable.ic_gps_not_fixed_black_36dp);
        todaysh.setIcon(R.drawable.ic_gps_fixed_black_36dp);
        tomsh.setIcon(R.drawable.ic_today_black_36dp);
        upd.setIcon(R.drawable.ic_system_update_alt_black_36dp);
        git.setIcon(R.drawable.ic_code_black_36dp);
        feed.setIcon(R.drawable.ic_near_me_black_36dp);
        stting.setIcon(R.drawable.ic_settings_applications_black_36dp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (darkOn) {
                darkon();
            } else {
                darkoff();
            }
            upcoming.setChecked(true);
            set_view(2, true);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(fromCampus.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        initialize();
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Log.e("DAYNAME", day);
        start_json();
    }

    void set_view(int idx, boolean mark) {
        Adapter_1 adapter_1;
        if (idx == 0) adapter_1 = new Adapter_1(fromCampus.this, arrayList1, mark);
        else if (idx == 2) adapter_1 = new Adapter_1(fromCampus.this, vector, mark);
        else adapter_1 = new Adapter_1(fromCampus.this, arrayList2, mark);
        linearLayoutManager = new LinearLayoutManager(fromCampus.this, RecyclerView.VERTICAL, false);
        //GridLayoutManager giid=new GridLayoutManager(fromCampus.this,2,RecyclerView.VERTICAL,false);
        recyclerView.setAdapter(adapter_1);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    void readSettings() {
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
            updateSettings();
        }
    }

    void updateSettings() {
        if (darkOn)
            json_str = "{\"dark\":1}";
        else
            json_str = "{\"dark\":0}";
        textFileHandler.WRITE_TEXT("settings.txt", json_str);
    }
    void start_json() {
        readSettings();
        Pair<Boolean, String> json = textFileHandler.READ_TEXT("Test.txt");
        if (json.first == Boolean.TRUE) {
            json_str = json.second;
            update(day_now);
        } else {
            update_json_data();
        }
        //log_json_arra();
        set_view(2, true);
        Toast.makeText(fromCampus.this, "Double Tap on Item to see Notes", Toast.LENGTH_LONG).show();
    }

    private void initialize() {
        navigationView = findViewById(R.id.nav_view);
        colorStateList1 = navigationView.getItemIconTintList();
        colorStateList2 = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        this.getResources().getColor(R.color.DarkRed),
                        this.getResources().getColor(R.color.Black)
                }
        );
        linearLayoutback = findViewById(R.id.linear_layout_main);
        upcoming = findViewById(R.id.drawer_menu_upcoming);
        fromku = findViewById(R.id.drawer_menu_from_KUET);
        toku = findViewById(R.id.drawer_menu_To_KUET);
        todaysh = findViewById(R.id.drawer_menu_full_list);
        tomsh = findViewById(R.id.drawer_menu_next_full_list);
        upd = findViewById(R.id.drawer_menu_update);
        git = findViewById(R.id.drawer_menu_github);
        feed = findViewById(R.id.drawer_menu_feedback);
        stting = findViewById(R.id.drawer_menu_settings);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        timce = new time_class();
        day = timce.get_day();
        Log.e("DAYY_BUG", day);
        next_day = timce.get_next_day(day);
        Log.e("DAYY_BUG", next_day);
        day_now = day;
        arrayList1 = new Vector<>();
        arrayList2 = new Vector<>();
        vector = new Vector<>();
        textFileHandler = new TextFileHandler();
        recyclerView = findViewById(R.id.recycler_view_main);
    }

    void log_json_arra() {
        for (int i = 0; i < vector.size(); i++) {
            log_bus(vector.get(i));
        }
    }

    void update(String day) {
        if (day.equals("FRIDAY")) return;
        try {
            JSONArray jsonArray = new JSONArray(json_str);
            for (int i = 0; i < jsonArray.length(); i++) {
                bus_data bsd = new bus_data();
                bus_data bsd1 = new bus_data();
                JSONObject tmp = jsonArray.getJSONObject(i);
                bsd.type = tmp.getString("TripName");
                bsd1.type = tmp.getString("TripName");

                bsd.loc1 = "KUET";
                bsd.loc1 = "KUET";

                bsd.time1 = tmp.getString("StartingTimefromCampus");
                bsd1.time1 = tmp.getString("StartingTimefromCampus");

                String xd = tmp.getString("StartingSpotTime");
                bsd.loc2 = process_location(xd);
                bsd1.loc2 = process_location(xd);

                bsd.time2 = process_time(xd);
                bsd1.time2 = process_time(xd);
                if (bsd.time1.length() < 5 || bsd.time2.length() < 5 || bsd1.time1.length() < 5 || bsd1.time2.length() < 5)
                    continue;
                if (!is_time(bsd.time1) || !is_time(bsd.time2) || !is_time(bsd1.time2) || !is_time(bsd.time1))
                    continue;
                bsd.msg = tmp.getString("Remarks");
                bsd1.msg = tmp.getString("Remarks");
                if (bsd.loc2.isEmpty()) {
                    xd = process_note_for_location(bsd.msg);
                    bsd.loc2 = xd;
                    bsd1.loc2 = xd;
                }
                bsd.from_campus = true;
                bsd1.from_campus = false;
                bsd.Section = tmp.getString("Section");
                bsd.Section = tmp.getString("Section");
                if ((day.equals("SATURDAY") && bsd.Section.equals("Saturday")) || (!day.equals("SATURDAY") && !bsd.Section.equals("Saturday"))) {
                    arrayList1.add(bsd);
                    arrayList2.add(bsd1);
                    vector.add(bsd);
                    vector.add(bsd1);
                }
            }

            Comparator<bus_data> E = new Comparator<bus_data>() {
                @Override
                public int compare(bus_data o1, bus_data o2) {
                    Date tim1, tim2;
                    if (o1.from_campus) {
                        tim1 = timce.get_time(o1.time1);
                    } else
                        tim1 = timce.get_time(o1.time2);
                    if (o2.from_campus) {
                        tim2 = timce.get_time(o2.time1);
                    } else
                        tim2 = timce.get_time(o2.time2);
                    return tim1.compareTo(tim2);
                }
            };
            Collections.sort(arrayList1, E);
            Collections.sort(arrayList2, E);
            Collections.sort(vector, E);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    String process_note_for_location(String s) {
        //StringBuilder ans=new StringBuilder();
        if (s.contains("Ferighat") || s.contains("ferighat")) return "Ferighat";
        if (s.contains("Rupsha") || s.contains("rupsha")) return "Rupsha";
        if (s.contains("Moylapota")) return "Moylapota";
        if (s.contains("Dakbangla")) return "Dakbangla";
        if (s.contains("Sonadanga")) return "Sonadanga";
        if (s.contains("Gollamari")) return "Gollamari";
        return "Dakbangla";
        //else return  ans.toString();
    }

    private void update_json_data() {
        GetBusData getBusData = new GetBusData();
        getBusData.execute();
    }

    private class GetBusData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(fromCampus.this);
            progressDialog.setMessage("Updating");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsnStr = sh.makeServiceCall(API);
            try {
                Log.e("JSON", jsnStr);
            } catch (Exception e) {
                Log.e("JSONERROR", e.getMessage());
            }
            if (jsnStr != null) {
                textFileHandler.WRITE_TEXT("Test.txt", jsnStr);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            json_str = textFileHandler.READ_TEXT("Test.txt").second;
            Toast.makeText(fromCampus.this, "DONE", Toast.LENGTH_SHORT).show();
            Log.e("DAYY", day_now);
            update(day);
            set_view(2, true);
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
            if (!Character.isAlphabetic(a.charAt(i))) break;
        }
        Log.e("OKKK", a.substring(0, i));
        Log.e("OKKK", String.valueOf(i));
        return a.substring(0, i);
    }

    void log_bus(bus_data a) {
        Log.e("WHAT", a.loc1);
        Log.e("WHAT", a.loc2);
        Log.e("WHAT", a.time1);
        Log.e("WHAT", a.time2);
        Log.e("WHAT", a.msg);
        Log.e("WHAT", a.type);
    }

    boolean is_time(String time_) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        Log.e("TIME", time_);
        try {
            format.parse(time_);
            return true;
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
            return false;
        }
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
            if (bsd.time1.length() < 5 || bsd.time2.length() < 5 || bsd1.time1.length() < 5 || bsd1.time2.length() < 5)
                continue;
            if (!is_time(bsd.time1) || !is_time(bsd.time2) || !is_time(bsd1.time2) || !is_time(bsd.time1))
                continue;
            bsd.msg = tmp.getString("index3");
            bsd1.msg = tmp.getString("index3");
            if (bsd.loc2.isEmpty()) {
                xd = process_note_for_location(bsd.msg);
                bsd.loc2 = xd;
                bsd1.loc2 = xd;
            }
            //  Log.e("KUETBUS_TIME",bsd.time1);
            // Log.e("KUETBUS_TIME",bsd.time2);
            bsd.from_campus = true;
            bsd1.from_campus = false;

            arrayList1.add(bsd);
            arrayList2.add(bsd1);
            vector.add(bsd);
            vector.add(bsd1);
        }

        Comparator<bus_data> E = new Comparator<bus_data>() {
            @Override
            public int compare(bus_data o1, bus_data o2) {
                Date tim1, tim2;
                if (o1.from_campus) {
                    tim1 = timce.get_time(o1.time1);
                } else
                    tim1 = timce.get_time(o1.time2);
                if (o2.from_campus) {
                    tim2 = timce.get_time(o2.time1);
                } else
                    tim2 = timce.get_time(o2.time2);
                return tim1.compareTo(tim2);
            }
        };
        Collections.sort(arrayList1, E);
        Collections.sort(arrayList2, E);
        Collections.sort(vector, E);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.drawer_menu_upcoming) {
            if (!day_now.equals(day)) {
                update(day);
                day_now = day;
            }
            set_view(2, true);

        } else if (id == R.id.drawer_menu_full_list) {
            if (!day_now.equals(day)) {
                update(day);
                day_now = day;
            }
            set_view(2, false);
        } else if (id == R.id.drawer_menu_To_KUET) {
            if (!day_now.equals(day)) {
                update(day);
                day_now = day;
            }
            set_view(1, true);
        } else if (id == R.id.drawer_menu_from_KUET) {
            if (!day_now.equals(day)) {
                update(day);
                day_now = day;
            }
            set_view(0, true);
        } else if (id == R.id.drawer_menu_update) {
            update_json_data();
        } else if (id == R.id.drawer_menu_next_full_list) {
            day_now = next_day;
            update(next_day);
            // log_json_arra();
            set_view(2, false);
        } else if (id == R.id.drawer_menu_github) {
            String url = "https://github.com/sabertooth9/KUET-BUS.git";
            Uri webpage = Uri.parse(url);
            Intent intn = new Intent(Intent.ACTION_VIEW, webpage);
            if (intn.resolveActivity(getPackageManager()) != null) {
                startActivity(intn);
            }
        } else if (id == R.id.drawer_menu_feedback) {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nuhash1083s@gmail.com"});
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            Email.putExtra(Intent.EXTRA_TEXT, "Hello Developer Kuet Bus's...," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
        } else if (id == R.id.drawer_menu_settings) {
            Intent intent = new Intent(fromCampus.this, SettingsActivity.class);
            startActivityForResult(intent, 1);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            set_view(2, true);
            //upcoming.setChecked(true);
        }

    }
}
