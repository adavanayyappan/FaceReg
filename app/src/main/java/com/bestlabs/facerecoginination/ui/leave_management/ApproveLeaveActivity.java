package com.bestlabs.facerecoginination.ui.leave_management;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.LeaveManagementRVAdapter;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.others.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ApproveLeaveActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    private Boolean isUndo = false;
    private LeaveManagementRVAdapter leaveManagementRVAdapter;
    ArrayList<LeaveModel> leaveModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);

        setUpToolBar();
        leaveModels = new ArrayList<>();
        initData();

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.rc_approve_leave);
        leaveManagementRVAdapter = new LeaveManagementRVAdapter(leaveModels,this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(leaveManagementRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        enableSwipeToCompleteAndUndo();
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
                isUndo = true;
                final int position = viewHolder.getAdapterPosition();
                final LeaveModel item = leaveManagementRVAdapter.getLeaveModels().get(position);

                leaveManagementRVAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(recyclerView, "Leave Request Approved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Approve", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isUndo) {
                            leaveManagementRVAdapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                            isUndo = false;
                        }
                    }
                });
                snackbar.setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isUndo) {
                            leaveManagementRVAdapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                            isUndo = false;
                        }
                    }
                });

                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
        LeaveModel leaveModel = new LeaveModel("Raganar","I'm sick","emp123","10/11/2019", "Approved");
        leaveModels.add(leaveModel);

        leaveModel = new LeaveModel("Suresh","I'm sick","emp123","10/11/2019", "Pending");
        leaveModels.add(leaveModel);
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
