package com.bestlabs.facerecoginination.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.ClaimRequestListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ClaimManagementSelectRVAdapter  extends RecyclerView.Adapter<ClaimManagementSelectRVAdapter.ViewHolder>{

    private ArrayList<ClaimRequestListResponse.Claim> claimModels;
    private Context mContext;
    private ClaimManagementSelectRVAdapter.OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ArrayList<ClaimRequestListResponse.Claim> getClaimModels() {
        return claimModels;
    }

    public ClaimManagementSelectRVAdapter(ArrayList<ClaimRequestListResponse.Claim> claimModels, Context mContext, ClaimManagementSelectRVAdapter.OnItemClickListener listener) {
        this.claimModels = claimModels;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClaimManagementSelectRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_allowance_item,parent,false);
        return (new ClaimManagementSelectRVAdapter.ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimManagementSelectRVAdapter.ViewHolder holder, int position) {
        ClaimRequestListResponse.Claim claim = claimModels.get(position);
        holder.titleTv.setText(claim.getClaimGroupName());
        holder.statusTv.setText(claim.getClaimStatus());
        holder.startTv.setText("Claim Amount: "+ claim.getClaimAmount());


        if ("Approved".equals(claim.getClaimStatus())) {
            // Set green background for Approved status
            holder.statusTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
        } else if ("Pending".equals(claim.getClaimStatus())) {
            // Set green background for Approved status
            holder.statusTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_orange_light));
        } else {
            // Set red background for other statuses
            holder.statusTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
        }

        try {
            Log.e("Tag", ""+claim.getClaimCreatedOn());
            Date date = stringToDate(claim.getClaimCreatedOn());

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

    public void restoreItem(ClaimRequestListResponse.Claim item, int position) {
        claimModels.add(position,item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTv, monthTv, titleTv, statusTv;
        TextView startTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.tv_leave_date);
            monthTv = itemView.findViewById(R.id.tv_leave_month);
            titleTv = itemView.findViewById(R.id.tv_leave_title);
            statusTv = itemView.findViewById(R.id.tv_leave_status);
            startTv = itemView.findViewById(R.id.tv_leave_start);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(claimModels.get(getAbsoluteAdapterPosition()).getClaimID());
                    notifyItemChanged(selectedPosition);
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition);
                }
            });
        }
    }

}
