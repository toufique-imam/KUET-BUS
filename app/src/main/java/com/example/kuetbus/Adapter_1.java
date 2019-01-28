package com.example.kuetbus;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

class viewholder extends RecyclerView.ViewHolder{
    TextInputEditText time,from,to,note,type;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        time=itemView.findViewById(R.id.text_view_start_time);
        from=itemView.findViewById(R.id.text_view_from);
        to=itemView.findViewById(R.id.text_view_to);
        note=itemView.findViewById(R.id.text_view_start_Note);
        type=itemView.findViewById(R.id.text_view_start_type);
    }
}

public class Adapter_1 extends RecyclerView.Adapter<viewholder>{
    String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String dateString = dateFormat.format(new Date()).toString();
        return  dateString;
    }
    Boolean comp(String bus_time,String cur_time){
        if(bus_time.length()<8){
            bus_time="0"+bus_time;
        }
        if(bus_time.endsWith("AM") && cur_time.endsWith("PM"))return false;
        if(bus_time.endsWith("PM") && cur_time.endsWith("AM"))return  true;
        if(bus_time.compareTo(cur_time)>=0)return true;
        return false;
    }
    Context ctx;
    boolean mark;
    Vector<bus_data> tmp_data;
    String time;
    public Adapter_1(Context ctx, Vector<bus_data> tmp_data,boolean mark) {
        this.ctx = ctx;
        this.mark = mark;
        time = getTime();
        Log.e("TIME", time);
        if(mark){
            this.tmp_data=new Vector<>();
            for(bus_data bus_data:tmp_data){
                if(bus_data.from_campus){
                    if(comp(bus_data.time1,time)){
                        this.tmp_data.add(bus_data);
                    }
                } else{
                    if(comp(bus_data.time2,time)){
                        this.tmp_data.add(bus_data);
                    }
                }
            }
            if(this.tmp_data.isEmpty()){
                bus_data bus_data=new bus_data();
                bus_data.time1="NO";
                bus_data.from_campus=true;
                bus_data.type="BUS";
                bus_data.loc1="IS";
                bus_data.loc2="AVAILABLE";
                bus_data.msg="RIGHT NOW IF YOU ARE USING FOR FIRST TIME PLEASE UPDATE";
                this.tmp_data.add(bus_data);
            }
        }
        else {
            this.tmp_data = tmp_data;
        }
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflat= LayoutInflater.from(ctx).inflate(R.layout.adapter_layout_bus_time,viewGroup,false);
        return  new viewholder(inflat);
    }
    void log_bus(bus_data a){
        Log.e("WHATHAP",a.loc1);
        Log.e("WHATHAP",a.loc2);
        Log.e("WHATHAP",a.time1);
        Log.e("WHATHAP",a.time2);
        Log.e("WHATHAP",a.msg);
        Log.e("WHATHAP",a.type);
    }
    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder, int i) {
        bus_data bus=tmp_data.get(i);
        //log_bus(bus);
        if(bus.from_campus) {
            viewholder.from.setText(bus.loc1);
            viewholder.to.setText(bus.loc2);
            viewholder.time.setText(bus.time1);
            viewholder.type.setText(bus.type);
            viewholder.note.setText(bus.msg);
        }
        else{
            viewholder.from.setText(bus.loc2);
            viewholder.to.setText(bus.loc1);
            viewholder.time.setText(bus.time2);
            viewholder.type.setText(bus.type);
            viewholder.note.setText(bus.msg);
        }
    }

    @Override
    public int getItemCount() {
        return tmp_data.size();
    }
}
