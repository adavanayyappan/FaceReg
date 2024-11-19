package com.bestlabs.facerecoginination.ui.leave_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.LeaveManagementRVAdapter;
import com.bestlabs.facerecoginination.adapters.LeaveTypeListRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.LeaveCategoryModel;
import com.bestlabs.facerecoginination.models.LeaveListModel;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.models.LeaveRequestListResponse;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveWorkerManagementFragment extends Fragment {

    static RecyclerView recyclerView, recyclerViewLeave;
    private Boolean isUndo = false;
    private LeaveManagementRVAdapter leaveManagementRVAdapter;
    private LeaveTypeListRVAdapter leaveTypeListRVAdapter;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton applyLeave_Btn;
    ArrayList<LeaveModel> leaveModels;
    private AlertDialog dialog;
    APIInterface apiService;
    private boolean isLoading = false;
    private int currentPage = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leave_worker, container, false);


        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        apiService = APIClient.getClient().create(APIInterface.class);

        applyLeave_Btn = root.findViewById(R.id.fab_apply_leave);
        constraintLayout = root.findViewById(R.id.constraint_layout);
        recyclerView = root.findViewById(R.id.rc_worker_details);

        recyclerViewLeave = root.findViewById(R.id.rc_worker_leave_list);

        applyLeave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), LeaveWorkerApplyActivity.class);
                startActivity(intent);
            }
        });
        getLeaveListTypeData();
        getRequestLeaveListTypeData();
        return root;
    }

    private void getRequestLeaveListTypeData() {
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
            Call<LeaveRequestListResponse> call = apiService.getRequestLeaveList(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<LeaveRequestListResponse>() {
                @Override
                public void onResponse(Call<LeaveRequestListResponse> call, Response<LeaveRequestListResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        LeaveRequestListResponse leaveListModels = response.body();
                        if (leaveListModels.getResult() == null) {
                            return;
                        }
                        leaveManagementRVAdapter = new LeaveManagementRVAdapter((ArrayList<LeaveRequestListResponse.Leave>) leaveListModels.getResult(),getActivity());
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setAdapter(leaveManagementRVAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<LeaveRequestListResponse> call, Throwable t) {
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
                    getRequestLeaveListTypeData();
                }
            });
        }
    }

    private void getLeaveListTypeData() {
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
            Call<LeaveListModel> call = apiService.getLeaveList(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<LeaveListModel>() {
                @Override
                public void onResponse(Call<LeaveListModel> call, Response<LeaveListModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        LeaveListModel leaveListModels = response.body();
                        if (leaveListModels.getResult() == null) {
                            return;
                        }

                        leaveTypeListRVAdapter = new LeaveTypeListRVAdapter((ArrayList<LeaveListModel.LeaveType>) leaveListModels.getResult(), getActivity());
                        recyclerViewLeave.setNestedScrollingEnabled(false);
                        recyclerViewLeave.setAdapter(leaveTypeListRVAdapter);
                        recyclerViewLeave.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<LeaveListModel> call, Throwable t) {
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
                    getLeaveListTypeData();
                }
            });
        }
    }
}
