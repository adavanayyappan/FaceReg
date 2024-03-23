package com.bestlabs.facerecoginination.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.LeaveModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ClaimManagementRVAdapter extends RecyclerView.Adapter<ClaimManagementRVAdapter.ViewHolder>{

    private ArrayList<ClaimModel> claimModels;
    private Context mContext;

    public ArrayList<ClaimModel> getClaimModels() {
        return claimModels;
    }

    public ClaimManagementRVAdapter(ArrayList<ClaimModel> claimModels, Context mContext) {
        this.mContext = mContext;
        this.claimModels = claimModels;
    }

    @NonNull
    @Override
    public ClaimManagementRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_allowance_item,parent,false);
        return (new ClaimManagementRVAdapter.ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimManagementRVAdapter.ViewHolder holder, int position) {
        ClaimModel claimModel = claimModels.get(position);
        holder.titleTv.setText(claimModel.getName());
        holder.statusTv.setText(claimModel.getStatus());

        if ("Approved".equals(claimModel.getStatus())) {
            // Set green background for Approved status
            holder.statusTv.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
        } else {
            // Set red background for other statuses
            holder.statusTv.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
        }

        try {
            Date date = stringToDate(claimModel.getDate());

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
        return claimModels.size();
    }

    public void removeItem(int position) {
        claimModels.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(ClaimModel item, int position) {
        claimModels.add(position,item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTv, monthTv, titleTv, statusTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.tv_claim_date);
            monthTv = itemView.findViewById(R.id.tv_claim_month);
            titleTv = itemView.findViewById(R.id.tv_allowance_title);
            statusTv = itemView.findViewById(R.id.tv_allowance_status);
        }
    }
}
