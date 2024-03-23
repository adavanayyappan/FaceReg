package com.bestlabs.facerecoginination.ui.claim;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.ClaimManagementRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ClaimSupervisorManagementFragment  extends Fragment {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private ClaimManagementRVAdapter claimManagementRVAdapter;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton applyClaim_Btn;
    ArrayList<ClaimModel> claimModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_claim_supervisor, container, false);

        claimModels = new ArrayList<>();
        initData();

        applyClaim_Btn = root.findViewById(R.id.fab_apply_claim);

        recyclerView = root.findViewById(R.id.rc_worker_details);
        claimManagementRVAdapter = new ClaimManagementRVAdapter(claimModels,getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(claimManagementRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        applyClaim_Btn.setOnClickListener(new View.OnClickListener() {
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
        option1.setText("Approve Worker Claim");
        option2.setText("Apply Own Claim");

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 1 click
                Intent intent = new Intent(getActivity().getBaseContext(), ApproveClaimActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 2 click
                Intent intent = new Intent(getActivity().getBaseContext(), ClaimWorkerApplyActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private void initData() {
        ClaimModel claimModel = new ClaimModel("Raganar","I'm sick","emp123","10/11/2019", "Approved");
        claimModels.add(claimModel);

        claimModel = new ClaimModel("Suresh","I'm sick","emp123","10/11/2019", "Pending");
        claimModels.add(claimModel);
    }
}


