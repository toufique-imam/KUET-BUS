package com.example.kuetbus;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.max;

public class time_class {
    time_class() {
    }

    Date get_time(String formatx) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        Log.e("TIME",formatx);
        try {
            return format.parse(formatx);
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    String get_day() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String ans = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        ans = ans.toUpperCase();
        return ans;
    }

    String get_next_day(String today) {
        if (today.equals("THURSDAY")) return "FRIDAY";
        if (today.equals("FRIDAY"))
            return "SATURDAY";
        else return "SUNDAY";
    }

    String get_time_str() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        return format.format(new Date());
    }

    Boolean comp(String bus_time, String cur_time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        Date time = format.parse(bus_time);
        Date cur = format.parse(cur_time);
        return time.compareTo(cur) >= 0;
    }

    String time_diff(String bus_time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        try {
            Date time = format.parse(bus_time);
            Date cur = format.parse(get_time_str());
            long dif = time.getTime() - cur.getTime();
            long diffm = dif / (60 * 1000) % 60;
            long diffHours = dif / (60 * 60 * 1000) % 24;
            diffm = max(diffm, 0);
            diffHours = max(diffHours, 0);
            return diffHours + "h:" + diffm + " min";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    Long time_diff_x(String bus_time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        try {
            Date time = format.parse(bus_time);
            Date cur = format.parse(get_time_str());
            return time.getTime() - cur.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
