package com.bestlabs.facerecoginination.ui.leave_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.LeaveManagementRVAdapter;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.ui.claim.ApproveClaimActivity;
import com.bestlabs.facerecoginination.ui.claim.ClaimWorkerApplyActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LeaveSupervisorManagementFragment extends Fragment {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private LeaveManagementRVAdapter leaveManagementRVAdapter;
    private ConstraintLayout constraintLayout;
    private CardView totalLeaveCard, annualLeaveCard, casualLeaveCard, sickLeaveCard;
    private TextView totalAppliedTv, annualAppliedTv, casualAppliedTv, sickAppliedTv;
    private TextView totalAvailableTv, annualAvailableTv, casualAvailableTv, sickAvailableTv;
    private FloatingActionButton applyLeave_Btn;
    ArrayList<LeaveModel> leaveModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leaver_supervisor, container, false);

        leaveModels = new ArrayList<>();
        initData();

        totalLeaveCard = root.findViewById(R.id.card_lay_total);
        annualLeaveCard = root.findViewById(R.id.card_lay_annual);
        casualLeaveCard = root.findViewById(R.id.card_lay_casual);
        sickLeaveCard = root.findViewById(R.id.card_lay_sick);
        applyLeave_Btn = root.findViewById(R.id.fab_apply_leave);

        totalAppliedTv = root.findViewById(R.id.tv_tot_applied_leave);
        annualAppliedTv = root.findViewById(R.id.tv_annual_applied_leave);
        casualAppliedTv = root.findViewById(R.id.tv_casual_applied_leave);
        sickAppliedTv = root.findViewById(R.id.tv_sick_applied_leave);

        totalAvailableTv = root.findViewById(R.id.tv_tot_remaining_leave);
        annualAvailableTv = root.findViewById(R.id.tv_annual_remaining_leave);
        casualAvailableTv = root.findViewById(R.id.tv_casual_remaining_leave);
        sickAvailableTv = root.findViewById(R.id.tv_sick_remaining_leave);

        recyclerView = root.findViewById(R.id.rc_worker_details);
        leaveManagementRVAdapter = new LeaveManagementRVAdapter(leaveModels,getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(leaveManagementRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        applyLeave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        return root;
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet);

        // Handle click events for items in the BottomSheetDialog
        TextView option1 = bottomSheetDialog.findViewById(R.id.option1);
        TextView option2 = bottomSheetDialog.findViewById(R.id.option2);
        option1.setText("Approve Worker Leave");
        option2.setText("Apply Own Leave");

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 1 click
                Intent intent = new Intent(getActivity().getBaseContext(), ApproveLeaveActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 2 click
                Intent intent = new Intent(getActivity().getBaseContext(), LeaveWorkerApplyActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private void initData() {
        LeaveModel leaveModel = new LeaveModel("Raganar","I'm sick","emp123","10/11/2019", "Approved");
        leaveModels.add(leaveModel);

        leaveModel = new LeaveModel("Suresh","I'm sick","emp123","10/11/2019", "Pending");
        leaveModels.add(leaveModel);
    }
}

