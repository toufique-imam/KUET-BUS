package com.example.kuetbus;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class viewholder extends RecyclerView.ViewHolder {
    TextInputEditText note, type;
    CountDownTimer timer;
    ImageButton alarm;
    TextView time,from,to;
    MaterialCardView cardView,incardtim,incardrem;
    LinearLayout linearLayout;
    TextView rem_time;
    ImageView img_from,img_to;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        alarm=itemView.findViewById(R.id.alarm_button);
        rem_time=itemView.findViewById(R.id.text_view_rem_time);
        linearLayout=itemView.findViewById(R.id.linear_layout_expand);
        cardView = itemView.findViewById(R.id.card_view_bus_time);
        time = itemView.findViewById(R.id.text_view_start_time);
        from = itemView.findViewById(R.id.text_view_from);
        to = itemView.findViewById(R.id.text_view_to);
        note = itemView.findViewById(R.id.text_view_start_Note);
        type = itemView.findViewById(R.id.text_view_start_type);
        img_from=itemView.findViewById(R.id.image_from);
        img_to=itemView.findViewById(R.id.image_to);
        incardtim=itemView.findViewById(R.id.card1);
        incardrem=itemView.findViewById(R.id.card2);
    }
}
