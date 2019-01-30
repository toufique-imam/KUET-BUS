package com.example.kuetbus;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class time_class {
    time_class(){}
    Date get_time(String formatx){
        DateFormat format=new SimpleDateFormat("hh:mm a",Locale.US);
        try {
            return format.parse(formatx);
        }catch (ParseException e){
            Log.e(getClass().getSimpleName(),e.getMessage());
        }
        return null;
    }
    String get_day(){
        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }
    String get_next_day(String today){
        if (today.equals("FRIDAY")) {
            return "SATURDAY";
        } else return "SUNDAY";
    }
    String get_time_str(){
        DateFormat dateFormat=new SimpleDateFormat("hh:mm a",Locale.US);
        return dateFormat.format(new Date());
    }
    Boolean comp(String bus_time, String cur_time) throws ParseException {
        DateFormat format=new SimpleDateFormat("hh:mm a",Locale.US);
        Date time=format.parse(bus_time);
        Date cur=format.parse(cur_time);
        if(time.compareTo(cur)>=0){
            return true;
        }
        return false;
    }
    String time_diff(String bus_time){
        DateFormat format=new SimpleDateFormat("hh:mm a",Locale.US);
        try {
            Date time=format.parse(bus_time);
            Date cur= format.parse(get_time_str());
            long dif=time.getTime()-cur.getTime();
            long diffm=dif/(60*1000)%60;
            long diffHours = dif / (60 * 60 * 1000) % 24;
            return  diffHours+"h:"+diffm+" min";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
