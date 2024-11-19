package com.bestlabs.facerecoginination.ui.leave_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.LeaveManagementRVAdapter;
import com.bestlabs.facerecoginination.adapters.LeaveManagementSelectRVAdapter;
import com.bestlabs.facerecoginination.models.LeaveRequestListResponse;
import com.bestlabs.facerecoginination.models.LeaveUpdateStatus;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SwipeToDeleteCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveSupervisorApproveFragment extends Fragment {

    static RecyclerView recyclerView;
    private LeaveManagementSelectRVAdapter leaveManagementSelectRVAdapter;
    private ConstraintLayout constraintLayout;
    private AlertDialog dialog;
    APIInterface apiService;
    int leaveID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_approve_leave, container, false);

        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);
        constraintLayout = root.findViewById(R.id.constraint_layout);
        apiService = APIClient.getClient().create(APIInterface.class);

        // Initialize RecyclerView and Adapter
        recyclerView = root.findViewById(R.id.rc_approve_leave);

        getApprovalLeaveListTypeData();

        return root;
    }

    private void enableSwipeToCompleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final LeaveRequestListResponse.Leave item = leaveManagementSelectRVAdapter.getLeaveModels().get(position);

                showBottomSheetDialog(item.getLeaveID());

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void showBottomSheetDialog(Integer claimID) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet);

        // Handle click events for items in the BottomSheetDialog
        TextView option1 = bottomSheetDialog.findViewById(R.id.option1);
        TextView option2 = bottomSheetDialog.findViewById(R.id.option2);
        option1.setText("Approve");
        option2.setText("Reject");

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 1 click
                postLeaveStatusRequest("Approve", claimID, "");
                bottomSheetDialog.dismiss();
                showRemarkAlertDialog(claimID);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 2 click
                bottomSheetDialog.dismiss();
                showRemarkAlertDialog(claimID);
            }
        });
        bottomSheetDialog.show();
    }

    private void showRemarkAlertDialog(Integer claimID) {
        // Create EditText
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter remark");

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Leave Remark")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String remark = editText.getText().toString();
                        postLeaveStatusRequest("Reject", claimID, remark);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getApprovalLeaveListTypeData() {
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
            Call<LeaveRequestListResponse> call = apiService.getApprovalLeaveList(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<LeaveRequestListResponse>() {
                @Override
                public void onResponse(Call<LeaveRequestListResponse> call, Response<LeaveRequestListResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        LeaveRequestListResponse leaveListModels = response.body();
                        if (leaveListModels.getResult() == null) {
                            return;
                        }
                        leaveManagementSelectRVAdapter = new LeaveManagementSelectRVAdapter((ArrayList<LeaveRequestListResponse.Leave>) leaveListModels.getResult(), getActivity(), new LeaveManagementSelectRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                showBottomSheetDialog(position);
                            }
                        });
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setAdapter(leaveManagementSelectRVAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                    getApprovalLeaveListTypeData();
                }
            });
        }
    }

    // Method to make the POST request
    public void postLeaveStatusRequest(String actionType, Integer leaveI, String remarks) {
        String token = PreferenceManager.getString(getActivity(), Constants.KEY_TOKEN, "");
        int empID = PreferenceManager.getInt(getActivity(), Constants.KEY_EMP_ID, 0);
        int clientID = PreferenceManager.getInt(getActivity(), Constants.KEY_CLIENT_ID, 0);
        String empID_STR = Base64Utils.intToBase64(empID);
        String clientID_STR = Base64Utils.intToBase64(clientID);
        Log.e("token", ""+token);

        Call<LeaveUpdateStatus> call = apiService.postLeaveStatus(token, empID_STR, clientID_STR, leaveID, actionType, remarks);

        call.enqueue(new Callback<LeaveUpdateStatus>() {
            @Override
            public void onResponse(Call<LeaveUpdateStatus> call, Response<LeaveUpdateStatus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    LeaveUpdateStatus leaveUpdateStatus = response.body();
                    Log.e("Response", ""+response.body());
                    Log.e("Status", leaveUpdateStatus.getStatus());

                    if (leaveUpdateStatus.getStatus().equals("success")) {
                        Toast.makeText(getActivity(), "Status updated successfully", Toast.LENGTH_SHORT).show();
                        getApprovalLeaveListTypeData();
                    } else {
                        Toast.makeText(getActivity(), "Status updated failed. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful response
                    // Example of using the AlertDialogHelper to show an alert dialog
                    Toast.makeText(getActivity(), "Status updated failed. Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LeaveUpdateStatus> call, Throwable t) {
                // Handle failure
                Toast.makeText(getActivity(), "Status updated failed. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
