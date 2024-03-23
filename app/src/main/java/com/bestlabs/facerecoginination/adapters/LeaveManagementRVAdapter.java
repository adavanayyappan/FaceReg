package com.bestlabs.facerecoginination.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LeaveManagementRVAdapter extends RecyclerView.Adapter<LeaveManagementRVAdapter.ViewHolder>{

    private ArrayList<LeaveModel> leaveModels;
    private Context mContext;

    public ArrayList<LeaveModel> getLeaveModels() {
        return leaveModels;
    }

    public LeaveManagementRVAdapter(ArrayList<LeaveModel> leaveModels, Context mContext) {
        this.leaveModels = leaveModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_leave_item,parent,false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveModel leaveModel = leaveModels.get(position);
        holder.titleTv.setText(leaveModel.getName());
        holder.statusTv.setText(leaveModel.getStatus());

        if ("Approved".equals(leaveModel.getStatus())) {
            // Set green background for Approved status
            holder.statusTv.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
        } else {
            // Set red background for other statuses
            holder.statusTv.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
        }

        try {
            Date date = stringToDate(leaveModel.getDate());

            // Format the date to "MMM" (month abbreviation)
            String formattedMonth = formatMonthDate(date);
            String formattedDate = formatDate(date);
            holder.monthTv.setText(formattedMonth);
            holder.dateTv.setText(formattedDate);

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

    private static String formatMonthDate(Date date) {
        // Define the desired format
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);

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
        return leaveModels.size();
    }

    public void removeItem(int position) {
        leaveModels.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(LeaveModel item, int position) {
        leaveModels.add(position,item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTv, monthTv, titleTv, statusTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.tv_leave_date);
            monthTv = itemView.findViewById(R.id.tv_leave_month);
            titleTv = itemView.findViewById(R.id.tv_leave_title);
            statusTv = itemView.findViewById(R.id.tv_leave_status);
        }
    }

}
