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
import com.bestlabs.facerecoginination.models.LeaveListModel;
import com.bestlabs.facerecoginination.models.LeaveModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LeaveTypeListRVAdapter  extends RecyclerView.Adapter<LeaveTypeListRVAdapter.ViewHolder>{

    private ArrayList<LeaveListModel.LeaveType> leaveListModel;
    private Context mContext;

    public ArrayList<LeaveListModel.LeaveType> getLeaveModels() {
        return leaveListModel;
    }

    public LeaveTypeListRVAdapter(ArrayList<LeaveListModel.LeaveType> leaveListModel, Context mContext) {
        this.leaveListModel = leaveListModel;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LeaveTypeListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_leave_list_item,parent,false);
        return (new LeaveTypeListRVAdapter.ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveTypeListRVAdapter.ViewHolder holder, int position) {
        LeaveListModel.LeaveType leaveModel = leaveListModel.get(position);
        holder.leaveValueTv.setText(""+leaveModel.getBalanceleave());
        holder.leaveTypeTv.setText(leaveModel.getLabel());
    }



    @Override
    public int getItemCount() {
        return leaveListModel.size();
    }

    public void removeItem(int position) {
        leaveListModel.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(LeaveListModel.LeaveType item, int position) {
        leaveListModel.add(position,item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView leaveValueTv, leaveTypeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveValueTv = itemView.findViewById(R.id.tv_tot_applied_leave);
            leaveTypeTv = itemView.findViewById(R.id.tv_leave_type);
        }
    }
}

