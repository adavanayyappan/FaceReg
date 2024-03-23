package com.bestlabs.facerecoginination.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.models.TimeSheetModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimeManagementAdapter extends RecyclerView.Adapter<TimeManagementAdapter.ViewHolder>{

    private ArrayList<TimeSheetModel> timeSheetModels = new ArrayList<>();
    private Context mContext;

    public ArrayList<TimeSheetModel> getTimeSheetModels() {
        return timeSheetModels;
    }

    public TimeManagementAdapter(Context mContext, ArrayList<TimeSheetModel> timeSheetModels) {
        this.mContext = mContext;
        this.timeSheetModels = timeSheetModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_time_item,parent,false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSheetModel timeSheetModel = timeSheetModels.get(position);

        try {
            Date date = stringToDate(timeSheetModel.getDate());

            String formattedYear = formatYearDate(date);
            String formattedDate = formatDate(date);
            holder.dateTv.setText(formattedDate);
            holder.dayYearTv.setText(formattedYear);
            holder.startTv.setText(timeSheetModel.getStartTime());
            holder.endTv.setText(timeSheetModel.getEndTime());
            holder.totalTv.setText(timeSheetModel.getTotalHours());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Date stringToDate(String dateString) throws ParseException {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        // Parse the date string into a Date object
        return dateFormat.parse(dateString);
    }

    private static String formatYearDate(Date date) {
        // Define the desired format
        SimpleDateFormat monthFormat = new SimpleDateFormat("EEE, MMM yyyy", Locale.ENGLISH);

        // Format the date
        return monthFormat.format(date);
    }

    private static String formatDate(Date date) {
        // Define the desired format
        SimpleDateFormat monthFormat = new SimpleDateFormat("dd", Locale.ENGLISH);

        // Format the date
        return monthFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return timeSheetModels.size();
    }

    public void removeItem(int position) {
        timeSheetModels.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(TimeSheetModel item, int position) {
        timeSheetModels.add(position,item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTv, dayYearTv, startTv, endTv, totalTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.tv_time_date);
            dayYearTv = itemView.findViewById(R.id.tv_time_day_year);
            startTv = itemView.findViewById(R.id.tv_time_start);
            endTv = itemView.findViewById(R.id.tv_time_end);
            totalTv = itemView.findViewById(R.id.tv_time_total);
        }
    }

}

