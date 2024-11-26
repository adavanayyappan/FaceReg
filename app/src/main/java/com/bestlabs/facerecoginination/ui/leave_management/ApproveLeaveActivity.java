package com.bestlabs.facerecoginination.ui.leave_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.LeaveManagementRVAdapter;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.models.LeaveRequestListResponse;
import com.bestlabs.facerecoginination.models.LeaveUpdateStatus;
import com.bestlabs.facerecoginination.models.PunchModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SwipeToDeleteCallback;
import com.bestlabs.facerecoginination.ui.claim.ApproveClaimActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveLeaveActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    private LeaveManagementRVAdapter leaveManagementRVAdapter;
    private ConstraintLayout constraintLayout;
    private AlertDialog dialog;
    APIInterface apiService;
    int leaveID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);

        setUpToolBar();

        ProgressBar progressBar = new ProgressBar(ApproveLeaveActivity.this);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ApproveLeaveActivity.this);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);
        constraintLayout = findViewById(R.id.constraint_layout);
        apiService = APIClient.getClient().create(APIInterface.class);

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.rc_approve_leave);

        Intent mIntent = getIntent();
        leaveID = mIntent.getIntExtra("leaveId", 0);

        enableSwipeToCompleteAndUndo();
        getApprovalLeaveListTypeData();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int titleTextColor = Color.WHITE; // Change it to the color you desire
        toolbar.setTitleTextColor(titleTextColor);
        setTitle("Approve Leave");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back); // Replace ic_arrow_back with your back arrow icon
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void enableSwipeToCompleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final LeaveRequestListResponse.Leave item = leaveManagementRVAdapter.getLeaveModels().get(position);

                showBottomSheetDialog(item.getLeaveID());

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void showBottomSheetDialog(Integer claimID) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ApproveLeaveActivity.this);
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
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 2 click
                showRemarksDialog(claimID);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private void showRemarksDialog(Integer claimID) {
        // Create an EditText
        final EditText input = new EditText(ApproveLeaveActivity.this);
        input.setHint("Enter your remarks");

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ApproveLeaveActivity.this);
        builder.setTitle("Enter Remarks")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the text from EditText
                        String remarks = input.getText().toString();
                        // Handle the remarks (e.g., save it, display it, etc.)
                        // You can use it here as needed
                        postLeaveStatusRequest("Reject", claimID, remarks);
                    }
                })
                .setNegativeButton("Cancel", null)  // Dismiss the dialog
                .show();
    }


    private void getApprovalLeaveListTypeData() {
        // Check if the internet is available
        if (NetworkUtils.isNetworkAvailable(ApproveLeaveActivity.this)) {
            // Your network-related logic here
            dialog.show();
            String token = PreferenceManager.getString(ApproveLeaveActivity.this, Constants.KEY_TOKEN, "");
            int empID = PreferenceManager.getInt(ApproveLeaveActivity.this, Constants.KEY_EMP_ID, 0);
            int clientID = PreferenceManager.getInt(ApproveLeaveActivity.this, Constants.KEY_CLIENT_ID, 0);
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
                            finish();
                            return;
                        }
                        leaveManagementRVAdapter = new LeaveManagementRVAdapter((ArrayList<LeaveRequestListResponse.Leave>) leaveListModels.getResult(),ApproveLeaveActivity.this);
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setAdapter(leaveManagementRVAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ApproveLeaveActivity.this));

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
    public void postLeaveStatusRequest(String actionType, Integer leaveID, String remarks) {
        String token = PreferenceManager.getString(ApproveLeaveActivity.this, Constants.KEY_TOKEN, "");
        int empID = PreferenceManager.getInt(ApproveLeaveActivity.this, Constants.KEY_EMP_ID, 0);
        int clientID = PreferenceManager.getInt(ApproveLeaveActivity.this, Constants.KEY_CLIENT_ID, 0);
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
                        Toast.makeText(ApproveLeaveActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                        getApprovalLeaveListTypeData();
                    } else {
                        Toast.makeText(ApproveLeaveActivity.this, "Status updated failed. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful response
                    // Example of using the AlertDialogHelper to show an alert dialog
                    Toast.makeText(ApproveLeaveActivity.this, "Status updated failed. Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LeaveUpdateStatus> call, Throwable t) {
                // Handle failure
                Toast.makeText(ApproveLeaveActivity.this, "Status updated failed. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
