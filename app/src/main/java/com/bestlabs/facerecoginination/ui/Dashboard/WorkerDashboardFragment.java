package com.bestlabs.facerecoginination.ui.Dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.activities.facereginationhelper.FaceAddActivity;
import com.bestlabs.facerecoginination.activities.facereginationhelper.FaceIdentifyActivity;
import com.bestlabs.facerecoginination.activities.facereginationhelper.FaceTwoIdentifyActivity;
import com.bestlabs.facerecoginination.adapters.TimeManagementAdapter;
import com.bestlabs.facerecoginination.models.PunchListModel;
import com.bestlabs.facerecoginination.models.PunchStatusModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.DateTimeUtils;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SimilarityClassifier;
import com.bestlabs.facerecoginination.ui.claim.ApproveClaimActivity;
import com.bestlabs.facerecoginination.ui.claim.ClaimWorkerApplyActivity;
import com.bestlabs.facerecoginination.ui.leave_management.LeaveWorkerApplyActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerDashboardFragment extends Fragment {

    private ConstraintLayout constraintLayout;
    private TextView tvHeader, tvTitleName, tvDesignation;
    private ImageView imageViewHeader, imageViewScan;
    private TextView tvDashTime, tvDashDate, tvCheckIn, tvCheckOut, tvTotal;

    private AlertDialog dialog;
    APIInterface apiService;
    int OUTPUT_SIZE=192;
    private HashMap<String, SimilarityClassifier.Recognition> registered = new HashMap<>(); //saved Faces
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_worker_dashboard, container, false);

        constraintLayout = root.findViewById(R.id.constraint_layout);
        tvHeader = root.findViewById(R.id.tv_header);
        tvTitleName = root.findViewById(R.id.tv_dash_name);
        tvDesignation = root.findViewById(R.id.tv_dash_designation);
        imageViewHeader = root.findViewById(R.id.floatingImageView);
        imageViewScan = root.findViewById(R.id.iv_dash_card_scan);

        tvDashTime = root.findViewById(R.id.tv_dash_card_time);
        tvDashDate = root.findViewById(R.id.tv_dash_card_date_year);
        tvCheckIn = root.findViewById(R.id.tv_card_check_in);
        tvCheckOut = root.findViewById(R.id.tv_card_check_out);
        tvTotal = root.findViewById(R.id.tv_card_check_total);

        String currentTime = DateTimeUtils.getCurrentDateTime("h:mm a");
        String currentDate = DateTimeUtils.getCurrentDateTime("EEE, MMM d");
        tvDashTime.setText(currentTime);
        tvDashDate.setText(currentDate);
        tvCheckIn.setText("- -:- -");
        tvCheckOut.setText("- -:- -");
        tvTotal.setText("- -:- -");

        String image_prefix = PreferenceManager.getString(getActivity(), Constants.KEY_EMP_IMAGE, "");
        String emp_name = PreferenceManager.getString(getActivity(), Constants.KEY_NAME, "");

        Picasso.get().load(Constants.KEY_IMAGE_URL+image_prefix).into(imageViewHeader);
        tvTitleName.setText(emp_name);

        imageViewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_prefix.isEmpty()) {
                    Intent intent = new Intent(getActivity().getBaseContext(), FaceAddActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity().getBaseContext(), FaceTwoIdentifyActivity.class);
                    startActivity(intent);
                }
            }
        });

        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        apiService = APIClient.getClient().create(APIInterface.class);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
         //Load saved faces from memory when app starts
        getPunchStatus();
    }

    private void getPunchStatus() {
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

            // Call the getPunchList method with authorization header and query parameters
            Call<PunchStatusModel> call = apiService.getPunchStatus(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<PunchStatusModel>() {
                @Override
                public void onResponse(Call<PunchStatusModel> call, Response<PunchStatusModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        PunchStatusModel punchListResponse = response.body();
                        if (punchListResponse.getStatus().equals("error")) {
                            Toast.makeText(getActivity(), "No Punch Time found.", Toast.LENGTH_SHORT);
                        } else {
                            Log.e("StartTime", ""+punchListResponse.getStartTime());
                            Log.e("EndTime", ""+punchListResponse.getEndTime());
                            // Handle the response data
                            tvCheckIn.setText(punchListResponse.getStartTime());
                            tvCheckOut.setText(punchListResponse.getEndTime());
                            tvTotal.setText(punchListResponse.getTotalHrs());
                        }
                    } else {
                        // Handle error response
                        Log.e("response", ""+response.errorBody());
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Oops Failed. Please Try Again", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<PunchStatusModel> call, Throwable t) {
                    // Handle failure
                    Log.e("response", ""+t.getLocalizedMessage());
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Oops Failed. Please Try Again", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet);

        // Handle click events for items in the BottomSheetDialog
        TextView option1 = bottomSheetDialog.findViewById(R.id.option1);
        TextView option2 = bottomSheetDialog.findViewById(R.id.option2);
        option1.setText("Add Face");
        option2.setText("Punch In");

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 1 click
                Intent intent = new Intent(getActivity().getBaseContext(), FaceAddActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle option 2 click
                Intent intent = new Intent(getActivity().getBaseContext(), FaceIdentifyActivity.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
}

