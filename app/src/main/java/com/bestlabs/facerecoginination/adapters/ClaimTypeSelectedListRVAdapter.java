package com.bestlabs.facerecoginination.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.ClaimGroupModel;
import com.bestlabs.facerecoginination.models.LeaveListModel;

import java.util.ArrayList;

public class ClaimTypeSelectedListRVAdapter extends RecyclerView.Adapter<ClaimTypeSelectedListRVAdapter.ViewHolder>{

    private ArrayList<ClaimGroupModel.Result> claimListModel;
    private Context mContext;

    public ArrayList<ClaimGroupModel.Result> getLeaveModels() {
        return claimListModel;
    }

    private int selectedPosition = RecyclerView.NO_POSITION;
    private ClaimTypeSelectedListRVAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ClaimTypeSelectedListRVAdapter(ArrayList<ClaimGroupModel.Result> claimListModel, Context mContext, int selectedPosition, ClaimTypeSelectedListRVAdapter.OnItemClickListener listener) {
        this.claimListModel = claimListModel;
        this.selectedPosition = selectedPosition;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClaimTypeSelectedListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_claim_list_item,parent,false);
        return (new ClaimTypeSelectedListRVAdapter.ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimTypeSelectedListRVAdapter.ViewHolder holder, int position) {
        ClaimGroupModel.Result claiResult = claimListModel.get(position);
        holder.leaveValueTv.setText(claiResult.getLabel());

        // Change background color based on selection
        holder.leaveValueTv.setTextColor(position == selectedPosition ? Color.WHITE : Color.parseColor("#203444"));
        holder.cardView.setBackgroundColor(position == selectedPosition ? Color.parseColor("#07162F") : Color.WHITE);
    }



    @Override
    public int getItemCount() {
        return claimListModel.size();
    }

    public void removeItem(int position) {
        claimListModel.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(ClaimGroupModel.Result item, int position) {
        claimListModel.add(position,item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView leaveValueTv;
        LinearLayout cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveValueTv = itemView.findViewById(R.id.tv_tot_applied_leave);
            cardView = itemView.findViewById(R.id.lv_lay_total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(claimListModel.get(getAbsoluteAdapterPosition()).getValue());
                    notifyItemChanged(selectedPosition);
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition);
                }
            });
        }
    }
}
