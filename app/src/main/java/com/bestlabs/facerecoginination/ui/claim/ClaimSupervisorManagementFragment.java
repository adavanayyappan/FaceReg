package com.bestlabs.facerecoginination.ui.claim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.ClaimManagementRVAdapter;
import com.bestlabs.facerecoginination.adapters.ClaimTypeSelectedListRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimGroupModel;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.ClaimRequestListResponse;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimSupervisorManagementFragment  extends Fragment {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private ClaimManagementRVAdapter claimManagementRVAdapter;
    private ClaimTypeSelectedListRVAdapter claimTypeSelectedListRVAdapter;
    static RecyclerView recyclerViewClaim;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton applyClaim_Btn;
    ArrayList<ClaimModel> claimModels;
    private AlertDialog dialog;
    APIInterface apiService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_claim_supervisor, container, false);



        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        apiService = APIClient.getClient().create(APIInterface.class);

        applyClaim_Btn = root.findViewById(R.id.fab_apply_claim);
        constraintLayout = root.findViewById(R.id.constraint_layout_cd);
        recyclerView = root.findViewById(R.id.rc_worker_details);
        recyclerViewClaim = root.findViewById(R.id.rc_worker_claim_list);

        applyClaim_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), ClaimWorkerApplyActivity.class);
                intent.putExtra("claimRequestID", 0);
                startActivity(intent);
            }
        });
        getRequestClaimTypeData();
        getClaimListTypeData();
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

    private void getRequestClaimTypeData() {
        // Check if the internet is available
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            // Your network-related logic here
            dialog.show();
            String token = PreferenceManager.getString(getActivity(), Constants.KEY_TOKEN, "");
            int empID = PreferenceManager.getInt(getActivity(), Constants.KEY_EMP_ID, 0);
            int clientID = PreferenceManager.getInt(getActivity(), Constants.KEY_CLIENT_ID, 0);
            String empID_STR = Base64Utils.intToBase64(empID);
            String clientID_STR = Base64Utils.intToBase64(clientID);
            Log.e("token", ""+token);
            Log.e("empID_STR", ""+empID_STR);
            Log.e("clientID_STR", ""+clientID_STR);
            // Call the getPunchList method with authorization header and query parameters
            Call<ClaimRequestListResponse> call = apiService.getRequestClaimList(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<ClaimRequestListResponse>() {
                @Override
                public void onResponse(Call<ClaimRequestListResponse> call, Response<ClaimRequestListResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        ClaimRequestListResponse claimRequestListResponse = response.body();
                        if (claimRequestListResponse.getResult() == null) {
                            return;
                        }
                        claimManagementRVAdapter = new ClaimManagementRVAdapter((ArrayList<ClaimRequestListResponse.Claim>) claimRequestListResponse.getResult(),getActivity());
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setAdapter(claimManagementRVAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<ClaimRequestListResponse> call, Throwable t) {
                    // Handle failure
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                }
            });
        } else {
            // Display Snackbar with retry option
            NetworkUtils.showNoInternetSnackbar(constraintLayout, new NetworkUtils.OnRetryListener() {
                @Override
                public void onRetry() {
                    // Handle retry action
                    getRequestClaimTypeData();
                }
            });
        }
    }

    private void getClaimListTypeData() {
        // Check if the internet is available
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            // Your network-related logic here
            dialog.show();
            String token = PreferenceManager.getString(getActivity(), Constants.KEY_TOKEN, "");
            int empID = PreferenceManager.getInt(getActivity(), Constants.KEY_EMP_ID, 0);
            int clientID = PreferenceManager.getInt(getActivity(), Constants.KEY_CLIENT_ID, 0);
            String empID_STR = Base64Utils.intToBase64(empID);
            String clientID_STR = Base64Utils.intToBase64(clientID);
            Log.e("token", ""+token);
            Log.e("empID_STR", ""+empID_STR);
            Log.e("clientID_STR", ""+clientID_STR);
            // Call the getPunchList method with authorization header and query parameters
            Call<ClaimGroupModel> call = apiService.getClaimList(token, empID_STR, clientID_STR, "dropdown");

            call.enqueue(new Callback<ClaimGroupModel>() {
                @Override
                public void onResponse(Call<ClaimGroupModel> call, Response<ClaimGroupModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        ClaimGroupModel claimGroupModel = response.body();
                        if (claimGroupModel.getResult() == null) {
                            return;
                        }
                        claimTypeSelectedListRVAdapter = new ClaimTypeSelectedListRVAdapter((ArrayList<ClaimGroupModel.Result>) claimGroupModel.getResult(), getActivity(), RecyclerView.NO_POSITION, new ClaimTypeSelectedListRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getActivity().getBaseContext(), ClaimWorkerApplyActivity.class);
                                intent.putExtra("claimRequestID", position);
                                startActivity(intent);
                            }
                        });
                        recyclerViewClaim.setNestedScrollingEnabled(false);
                        recyclerViewClaim.setAdapter(claimTypeSelectedListRVAdapter);
                        recyclerViewClaim.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<ClaimGroupModel> call, Throwable t) {
                    // Handle failure
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                }
            });
        } else {
            // Display Snackbar with retry option
            NetworkUtils.showNoInternetSnackbar(constraintLayout, new NetworkUtils.OnRetryListener() {
                @Override
                public void onRetry() {
                    // Handle retry action
                    getClaimListTypeData();
                }
            });
        }
    }
}


