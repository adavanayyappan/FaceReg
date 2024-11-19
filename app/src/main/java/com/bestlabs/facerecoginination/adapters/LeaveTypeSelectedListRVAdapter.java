package com.bestlabs.facerecoginination.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.LeaveListModel;

import java.util.ArrayList;



public class LeaveTypeSelectedListRVAdapter  extends RecyclerView.Adapter<LeaveTypeSelectedListRVAdapter.ViewHolder>{

    private ArrayList<LeaveListModel.LeaveType> leaveListModel;
    private Context mContext;

    public ArrayList<LeaveListModel.LeaveType> getLeaveModels() {
        return leaveListModel;
    }

    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public LeaveTypeSelectedListRVAdapter(ArrayList<LeaveListModel.LeaveType> leaveListModel, Context mContext, int selectedPosition, OnItemClickListener listener) {
        this.leaveListModel = leaveListModel;
        this.selectedPosition = selectedPosition;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeaveTypeSelectedListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_leave_list_item,parent,false);
        return (new LeaveTypeSelectedListRVAdapter.ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveTypeSelectedListRVAdapter.ViewHolder holder, int position) {
        LeaveListModel.LeaveType leaveModel = leaveListModel.get(position);
        holder.leaveValueTv.setText(""+leaveModel.getBalanceleave());
        holder.leaveTypeTv.setText(leaveModel.getLabel());

        // Change background color based on selection
        holder.leaveValueTv.setTextColor(position == selectedPosition ? Color.WHITE : Color.parseColor("#203444"));
        holder.leaveTypeTv.setTextColor(position == selectedPosition ? Color.WHITE : Color.parseColor("#203444"));
        holder.cardView.setBackgroundColor(position == selectedPosition ? Color.parseColor("#07162F") : Color.WHITE);
//        holder.cardView.setCardElevation(1);
//        holder.cardView.setRadius(16);
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
        LinearLayout cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveValueTv = itemView.findViewById(R.id.tv_tot_applied_leave);
            leaveTypeTv = itemView.findViewById(R.id.tv_leave_type);
            cardView = itemView.findViewById(R.id.lv_lay_total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(leaveListModel.get(getAbsoluteAdapterPosition()).getValue());
                    notifyItemChanged(selectedPosition);
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition);
                }
            });
        }
    }
}


