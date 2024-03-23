package com.bestlabs.facerecoginination.ui.TimeSheet;
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
import com.bestlabs.facerecoginination.adapters.TimeManagementAdapter;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.models.TimeSheetModel;
import com.bestlabs.facerecoginination.others.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeSheetFragment extends Fragment {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private TimeManagementAdapter timeManagementAdapter;
    private ConstraintLayout constraintLayout;
    ArrayList<TimeSheetModel> timeSheetModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_time_management, container, false);

        timeSheetModels = new ArrayList<>();
        initData();

        constraintLayout = root.findViewById(R.id.cl_time_management);
        recyclerView = root.findViewById(R.id.rc_worker_time_management);
        timeManagementAdapter = new TimeManagementAdapter(getContext(), timeSheetModels);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(timeManagementAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Check if the internet is available
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            // Your network-related logic here
        } else {
            // Display Snackbar with retry option
            NetworkUtils.showNoInternetSnackbar(root, new NetworkUtils.OnRetryListener() {
                @Override
                public void onRetry() {
                    // Handle retry action
                    // You may want to reattempt the network operation
                }
            });
        }

        return root;
    }

    private void getTimeSheetData() {
        // Create an instance of the Retrofit service
        APIInterface timeService = APIClient.getClient().create(APIInterface.class);

        // Make the API call
        Call<List<TimeSheetModel>> call = timeService.getTimeSheetData();
        call.enqueue(new Callback<List<TimeSheetModel>>() {
            @Override
            public void onResponse(Call<List<TimeSheetModel>> call, Response<List<TimeSheetModel>> response) {
                if (response.isSuccessful()) {
                    List<TimeSheetModel> leaves = response.body();

                    // Update your UI with the claims data
                    // For example, you can use RecyclerView to display a list of claims
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<TimeSheetModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void initData() {
        TimeSheetModel timeSheetModel = new TimeSheetModel("Adavan", "25-02-2024", "9:30 AM", "7:00 PM", "9:00");
        timeSheetModels.add(timeSheetModel);

        timeSheetModel = new TimeSheetModel("Adavan", "25-02-2024", "9:30 AM", "7:00 PM", "9:00");
        timeSheetModels.add(timeSheetModel);
    }

}

