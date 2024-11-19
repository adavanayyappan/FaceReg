package com.bestlabs.facerecoginination.ui.TimeSheet;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.TimeManagementAdapter;
import com.bestlabs.facerecoginination.models.PunchListModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeSheetFragment extends Fragment {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private TimeManagementAdapter timeManagementAdapter;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton filter_Btn;
    View root;
    private AlertDialog dialog;
    APIInterface apiService;
    private String filterMonth = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_time_management, container, false);

        constraintLayout = root.findViewById(R.id.cl_time_management);
        recyclerView = root.findViewById(R.id.rc_worker_time_management);
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        apiService = APIClient.getClient().create(APIInterface.class);
        filter_Btn = root.findViewById(R.id.fab_filter_timesheet);

        filter_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        getTimeSheetData();
        return root;
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the date picker's initial values
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Update the EditText with the selected date
                        filterMonth = selectedYear + "-" + (monthOfYear + 1);
                        getTimeSheetData();
                    }
                },
                year,
                month,
                day
        );

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void getTimeSheetData() {
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
            Call<PunchListModel> call = apiService.getPunchList(token, empID_STR, clientID_STR, filterMonth);

            call.enqueue(new Callback<PunchListModel>() {
                @Override
                public void onResponse(Call<PunchListModel> call, Response<PunchListModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        PunchListModel punchListResponse = response.body();
                        Log.e("response", ""+punchListResponse.getResult());
                        // Handle the response data
                        timeManagementAdapter = new TimeManagementAdapter(getContext(), punchListResponse);
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setAdapter(timeManagementAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        // Handle error response
                        Log.e("response", ""+response.errorBody());
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Oops Failed. Please Try Again", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<PunchListModel> call, Throwable t) {
                    // Handle failure
                    Log.e("response", ""+t.getLocalizedMessage());
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Oops Failed. Please Try Again", Toast.LENGTH_SHORT);
                }
            });
        }
    }
}

