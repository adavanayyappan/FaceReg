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

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.LeaveManagementRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveWorkerManagementFragment extends Fragment {

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
        View root = inflater.inflate(R.layout.fragment_leave_worker, container, false);

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
                Intent intent = new Intent(getActivity().getBaseContext(), LeaveWorkerApplyActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void getLeaveData() {
        // Create an instance of the Retrofit service
        APIInterface leaveService = APIClient.getClient().create(APIInterface.class);

        // Make the API call
        Call<List<LeaveModel>> call = leaveService.getLeaveData();
        call.enqueue(new Callback<List<LeaveModel>>() {
            @Override
            public void onResponse(Call<List<LeaveModel>> call, Response<List<LeaveModel>> response) {
                if (response.isSuccessful()) {
                    List<LeaveModel> leaves = response.body();

                    // Update your UI with the claims data
                    // For example, you can use RecyclerView to display a list of claims
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<LeaveModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void initData() {
        LeaveModel leaveModel = new LeaveModel("Raganar","I'm sick","emp123","10/11/2019", "Approved");
        leaveModels.add(leaveModel);

        leaveModel = new LeaveModel("Suresh","I'm sick","emp123","10/11/2019", "Pending");
        leaveModels.add(leaveModel);
    }
}
