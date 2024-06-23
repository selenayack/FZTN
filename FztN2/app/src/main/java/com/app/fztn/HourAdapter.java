package com.app.fztn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Context context;
    private ArrayList<String> hourList;

    public HourAdapter(Context context, ArrayList<String> hourList) {
        this.context = context;
        this.hourList = hourList;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hour_item, parent, false);
        return new HourViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        String hour = hourList.get(position);
        holder.hourButton.setText(hour);
        holder.hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Selected hour: " + hour, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }

    public static class HourViewHolder extends RecyclerView.ViewHolder {
        Button hourButton;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);
            hourButton = itemView.findViewById(R.id.hour_button);
        }
    }
}
