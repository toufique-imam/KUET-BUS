package com.example.kuetbus;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

class viewholder extends RecyclerView.ViewHolder {
    TextInputEditText time, from, to, note, type;
    MaterialCardView cardView;
    LinearLayout linearLayout;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        linearLayout=itemView.findViewById(R.id.linear_layout_expand);
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
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
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
                    try {
                        if (comp(bus_data.time1, time)) {
                            this.tmp_data.add(bus_data);
                        }
                    } catch (ParseException e) {
                        Log.e("TIMEERROR",e.getMessage());
                    }
                } else {
                    try {
                        if (comp(bus_data.time2, time)) {
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

    void log_bus(bus_data a) {
        Log.e("WHATHAP", a.loc1);
        Log.e("WHATHAP", a.loc2);
        Log.e("WHATHAP", a.time1);
        Log.e("WHATHAP", a.time2);
        Log.e("WHATHAP", a.msg);
        Log.e("WHATHAP", a.type);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder viewholder, final int i) {
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
        viewholder.linearLayout.setVisibility(View.GONE);
        viewholder.type.setVisibility(View.GONE);
        viewholder.note.setVisibility(View.GONE);
        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i);
            }
        });
        viewholder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i);
            }
        });
        viewholder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i);
            }
        });
        viewholder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i);
            }
        });
        viewholder.from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i);
            }
        });
        viewholder.to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_action(viewholder,i);
            }
        });
    }

    void Onclick_action(viewholder viewholder,int i) {
        if (viewholder.type.getVisibility() == View.GONE) {
            viewholder.type.setVisibility(View.VISIBLE);
            viewholder.note.setVisibility(View.VISIBLE);
            viewholder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            viewholder.type.setVisibility(View.GONE);
            viewholder.note.setVisibility(View.GONE);
            viewholder.linearLayout.setVisibility(View.GONE);
        }
       // notifyItemChanged(i);
    }

    @Override
    public int getItemCount() {
        return tmp_data.size();
    }
}
