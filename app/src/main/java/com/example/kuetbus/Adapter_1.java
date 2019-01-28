package com.example.kuetbus;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

class viewholder extends RecyclerView.ViewHolder {
    TextInputEditText time, from, to, note, type;
    MaterialCardView cardView;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_view_bus_time);
        time = itemView.findViewById(R.id.text_view_start_time);
        from = itemView.findViewById(R.id.text_view_from);
        to = itemView.findViewById(R.id.text_view_to);
        note = itemView.findViewById(R.id.text_view_start_Note);
        type = itemView.findViewById(R.id.text_view_start_type);
    }
}

public class Adapter_1 extends RecyclerView.Adapter<viewholder> {
    String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String dateString = dateFormat.format(new Date()).toString();
        return dateString;
    }

    String make_24(String time) {
        String ans;
        if (time.endsWith("PM")) {
            Integer x = Integer.valueOf(time.substring(0, 2));
            x += 12;
            ans = String.valueOf(x) + time.substring(2, 5);
        } else {
            ans = time.substring(0, 5);
        }
        return ans;
    }

    Boolean comp(String bus_time, String cur_time) {
        if (bus_time.length() < 8) {
            bus_time = "0" + bus_time;
        }
        bus_time = make_24(bus_time);
        cur_time = make_24(cur_time);
        Log.e("TIME_bus", bus_time);
        Log.e("TIME_cur", cur_time);
        if (bus_time.compareTo(cur_time) >= 0) {
            Log.e("TIME_bus", bus_time);
            Log.e("TIME_cur", cur_time);
            return true;
        }
        return false;
    }

    Context ctx;
    boolean mark;
    Vector<bus_data> tmp_data;
    String time;

    public Adapter_1(Context ctx, Vector<bus_data> tmp_data, boolean mark) {
        this.ctx = ctx;
        this.mark = mark;
        time = getTime();
        Log.e("TIME", time);
        if (mark) {
            this.tmp_data = new Vector<>();
            for (bus_data bus_data : tmp_data) {
                if (bus_data.from_campus) {
                    if (comp(bus_data.time1, time)) {
                        this.tmp_data.add(bus_data);
                    }
                } else {
                    if (comp(bus_data.time2, time)) {
                        this.tmp_data.add(bus_data);
                    }
                }
            }
            if (this.tmp_data.isEmpty()) {
                bus_data bus_data = new bus_data();
                bus_data.time1 = "NO";
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

    void log_bus(bus_data a) {
        Log.e("WHATHAP", a.loc1);
        Log.e("WHATHAP", a.loc2);
        Log.e("WHATHAP", a.time1);
        Log.e("WHATHAP", a.time2);
        Log.e("WHATHAP", a.msg);
        Log.e("WHATHAP", a.type);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder viewholder, int i) {
        bus_data bus = tmp_data.get(i);
        //log_bus(bus);
        if (bus.from_campus) {
            viewholder.from.setText(bus.loc1);
            viewholder.to.setText(bus.loc2);
            viewholder.time.setText(bus.time1);
            viewholder.type.setText(bus.type);
            viewholder.note.setText(bus.msg);
        } else {
            viewholder.from.setText(bus.loc2);
            viewholder.to.setText(bus.loc1);
            viewholder.time.setText(bus.time2);
            viewholder.type.setText(bus.type);
            viewholder.note.setText(bus.msg);
        }
        viewholder.type.setVisibility(View.GONE);
        viewholder.note.setVisibility(View.GONE);
        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder);
            }
        });
        viewholder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder);
            }
        });
        viewholder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder);
            }
        });
        viewholder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder);
            }
        });
        viewholder.from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder);
            }
        });
        viewholder.to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder);
            }
        });
    }

    void Onclick_action(viewholder viewholder) {
        if (viewholder.type.getVisibility() == View.GONE) {
            viewholder.type.setVisibility(View.VISIBLE);
            viewholder.note.setVisibility(View.VISIBLE);
        } else {
            viewholder.type.setVisibility(View.GONE);
            viewholder.note.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tmp_data.size();
    }
}
