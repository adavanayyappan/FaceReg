package com.bestlabs.facerecoginination.ui.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.activities.facereginationhelper.FaceAddActivity;
import com.bestlabs.facerecoginination.activities.facereginationhelper.FaceIdentifyActivity;

public class SupervisorDashboardFragment  extends Fragment {

    private ConstraintLayout constraintLayout;
    private TextView tvHeader, tvTitleName, tvDesignation;
    private ImageView imageViewHeader, imageViewScan;
    private TextView tvDashTime, tvDashDate, tvCheckIn, tvCheckOut, tvTotal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_supervisor_dashboard, container, false);

        tvHeader = root.findViewById(R.id.tv_header);
        tvTitleName = root.findViewById(R.id.tv_dash_name);
        tvDesignation = root.findViewById(R.id.tv_dash_designation);
        imageViewHeader = root.findViewById(R.id.imageView_top_header);
        imageViewScan = root.findViewById(R.id.iv_dash_card_scan);

        tvDashTime = root.findViewById(R.id.tv_dash_card_time);
        tvDashDate = root.findViewById(R.id.tv_dash_card_date_year);
        tvCheckIn = root.findViewById(R.id.tv_card_check_in);
        tvCheckOut = root.findViewById(R.id.tv_card_check_out);
        tvTotal = root.findViewById(R.id.tv_card_check_total);

        imageViewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), FaceIdentifyActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
