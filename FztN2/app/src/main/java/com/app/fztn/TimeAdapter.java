package com.app.fztn;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    private ArrayList<String> timeList;
    private ArrayList<Boolean> isTimeOccupiedList; // Burası eklendi
    private OnTimeClickListener onTimeClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnTimeClickListener {
        void onTimeClick(String time);
    }

    public TimeAdapter(ArrayList<String> timeList, ArrayList<Boolean> isTimeOccupiedList, OnTimeClickListener onTimeClickListener) {
        this.timeList = timeList;
        this.isTimeOccupiedList = isTimeOccupiedList;
        this.onTimeClickListener = onTimeClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = timeList.get(position);
        holder.textViewTime.setText(time);

        // Eğer pozisyon seçilmişse arka plan rengini değiştir
        // Eğer pozisyon seçilmişse arka plan rengini değiştir
        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.selected_time_background);
        } else {
            // Dolu olan saatler için dikdörtgen kırmızı arka plan kullan
            if (isTimeOccupiedList.get(position)) {
                holder.itemView.setBackgroundResource(R.drawable.occupied_time_background);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.default_time_background);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
            onTimeClickListener.onTimeClick(time);
        });
    }
    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}
