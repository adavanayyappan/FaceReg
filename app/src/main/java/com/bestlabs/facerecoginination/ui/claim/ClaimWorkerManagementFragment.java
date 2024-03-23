package com.bestlabs.facerecoginination.ui.claim;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.ClaimManagementRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimWorkerManagementFragment  extends Fragment {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private ClaimManagementRVAdapter claimManagementRVAdapter;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton applyClaim_Btn;
    ArrayList<ClaimModel> claimModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_claim_worker, container, false);

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
                Intent intent = new Intent(getActivity().getBaseContext(), ClaimWorkerApplyActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void getClaimData() {
        // Create an instance of the Retrofit service
        APIInterface claimService = APIClient.getClient().create(APIInterface.class);

        // Make the API call
        Call<List<ClaimModel>> call = claimService.getClaims();
        call.enqueue(new Callback<List<ClaimModel>>() {
            @Override
            public void onResponse(Call<List<ClaimModel>> call, Response<List<ClaimModel>> response) {
                if (response.isSuccessful()) {
                    List<ClaimModel> claims = response.body();

                    // Update your UI with the claims data
                    // For example, you can use RecyclerView to display a list of claims
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<ClaimModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void initData() {
        ClaimModel claimModel = new ClaimModel("Raganar","I'm sick","emp123","10/11/2019", "Approved");
        claimModels.add(claimModel);

        claimModel = new ClaimModel("Suresh","I'm sick","emp123","10/11/2019", "Pending");
        claimModels.add(claimModel);
    }
}

