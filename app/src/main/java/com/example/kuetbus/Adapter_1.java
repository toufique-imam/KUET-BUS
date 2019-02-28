package com.example.kuetbus;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.text.ParseException;
import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.kuetbus.R.drawable.ic_alarm_on_black_18dp;
import static com.example.kuetbus.R.drawable.ic_alarm_on_white_18dp;
import static com.example.kuetbus.fromCampus.darkOn;

public class Adapter_1 extends RecyclerView.Adapter<viewholder> {
    private Context ctx;
    private boolean mark;
    private Vector<bus_data> tmp_data;
    private String time;
    private time_class timce;
    private int cardcolto,cardcolfrom,incardto,incardfrom;
    private int imagefrom;
    private int imageto;
    public Adapter_1(Context ctx, Vector<bus_data> tmp_data, boolean mark) {
        this.ctx = ctx;
        timce=new time_class();
        this.mark = mark;
        if(darkOn) {
            imagefrom=R.drawable.ic_near_me_white_24dp;
            imageto=R.drawable.ic_gps_fixed_white_24dp;
            this.cardcolto = fromCampus.colorblue;
            this.cardcolfrom=fromCampus.colordark;
            this.incardfrom=fromCampus.whitesmoke;
            this.incardto=fromCampus.whitesmoke;
        }
        else{
            imagefrom=R.drawable.ic_near_me_black_24dp;
            imageto=R.drawable.ic_gps_fixed_black_24dp;
            this.cardcolto = fromCampus.whitesmoke;
            this.cardcolfrom=fromCampus.whitesmoke;
            this.incardfrom=fromCampus.colordark;
            this.incardto=fromCampus.colorblue;
        }
        this.time = timce.get_time_str();
        Log.e("TIME", time);
        if (mark) {
            this.tmp_data = new Vector<>();
            for (bus_data bus_data : tmp_data) {
                if (bus_data.from_campus) {
                    try {
                        if (timce.comp(bus_data.time1, time)) {
                            this.tmp_data.add(bus_data);
                        }
                    } catch (ParseException e) {
                        Log.e("TIMEERROR",e.getMessage());
                    }
                } else {
                    try {
                        if (timce.comp(bus_data.time2, time)) {
                            this.tmp_data.add(bus_data);
                        }
                    } catch (ParseException e) {
                        Log.e("TIMEERROR",e.getMessage());
                    }
                }
            }
            if (this.tmp_data.isEmpty()) {
                bus_data bus_data = new bus_data();
                bus_data.time1 = "NO BUS";
                bus_data.from_campus = true;
                bus_data.type = "BUS";
                bus_data.loc1 = "IS";
                bus_data.loc2 = "AVAILABLE";
                bus_data.msg = "RIGHT NOW IF YOU ARE USING FOR FIRST TIME PLEASE UPDATE";
                this.tmp_data.add(bus_data);
            }
        } else {
            this.tmp_data = tmp_data;
        }
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflat = LayoutInflater.from(ctx).inflate(R.layout.adapter_layout_bus_time, viewGroup, false);
        return new viewholder(inflat);
    }
    @Override
    public void onBindViewHolder(@NonNull final viewholder viewholder, final int i) {
        final bus_data bus = tmp_data.get(i);
        //log_bus(bus);
        viewholder.img_from.setImageResource(imagefrom);
        viewholder.img_to.setImageResource(imageto);
        if (bus.from_campus) {
            //------------Color---------//
            viewholder.cardView.setBackgroundColor(cardcolfrom);
            viewholder.time.setTextColor(cardcolfrom);
            viewholder.rem_time.setTextColor(cardcolfrom);
            viewholder.from.setTextColor(incardfrom);
            viewholder.to.setTextColor(incardfrom);
            viewholder.incardtim.setBackgroundColor(incardfrom);
            viewholder.incardrem.setBackgroundColor(incardfrom);
            viewholder.note.setTextColor(incardfrom);
            viewholder.type.setTextColor(incardfrom);
            viewholder.rem_time.setText(timce.time_diff(bus.time1));
            viewholder.from.setText(bus.loc1);
            viewholder.to.setText(bus.loc2);
            viewholder.time.setText(bus.time1);
            viewholder.type.setText(bus.type);
            viewholder.note.setText(bus.msg);
        } else {

            //------------Color---------//
            viewholder.cardView.setBackgroundColor(cardcolto);
            viewholder.time.setTextColor(cardcolto);
            viewholder.rem_time.setTextColor(cardcolto);
            viewholder.from.setTextColor(incardto);
            viewholder.to.setTextColor(incardto);
            viewholder.incardtim.setBackgroundColor(incardto);
            viewholder.incardrem.setBackgroundColor(incardto);
            viewholder.note.setTextColor(incardto);
            viewholder.type.setTextColor(incardto);

            //-----------Text----------//
            viewholder.rem_time.setText(timce.time_diff(bus.time2));
            viewholder.from.setText(bus.loc2);
            viewholder.to.setText(bus.loc1);
            viewholder.time.setText(bus.time2);
            viewholder.type.setText(bus.type);
            viewholder.note.setText(bus.msg);
        }
        viewholder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewholder.alarm.setBackgroundResource(ic_alarm_on_black_18dp);
            }
        });
        viewholder.linearLayout.setVisibility(View.GONE);
        viewholder.type.setVisibility(View.GONE);
        viewholder.note.setVisibility(View.GONE);
        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
        viewholder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
        viewholder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
        viewholder.rem_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
        viewholder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
        viewholder.from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
        viewholder.to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i,v);
            }
        });
    }

    private void Onclick_action(viewholder viewholder, int i,View v) {
        Animation animation1=AnimationUtils.loadAnimation(ctx, R.anim.slideup);
        Animation animation2=AnimationUtils.loadAnimation(ctx, R.anim.slidedown);
        if (viewholder.type.getVisibility() == View.GONE) {
            v.startAnimation(animation2);
        //    v.animate().translationY(v.getHeight()).setDuration(300);
            viewholder.type.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewholder.cardView.setElevation(100);
                viewholder.incardrem.setElevation(20);
                viewholder.incardtim.setElevation(20);
            }
            viewholder.note.setVisibility(View.VISIBLE);
            viewholder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            v.startAnimation(animation2);
            viewholder.type.setVisibility(View.GONE);
            viewholder.note.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewholder.cardView.setElevation(0);
                viewholder.incardrem.setElevation(0);
                viewholder.incardtim.setElevation(0);
            }
            viewholder.linearLayout.setVisibility(View.GONE);
        }
       // notifyItemChanged(i);
    }

    @Override
    public int getItemCount() {
        return tmp_data.size();
    }
}
