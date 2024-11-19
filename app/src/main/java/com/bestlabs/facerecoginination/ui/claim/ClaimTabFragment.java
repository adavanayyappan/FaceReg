package com.bestlabs.facerecoginination.ui.claim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.ui.leave_management.LeaveSupervisorApproveFragment;
import com.bestlabs.facerecoginination.ui.leave_management.LeaveSupervisorManagementFragment;
import com.bestlabs.facerecoginination.ui.leave_management.LeaveTabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ClaimTabFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private LeaveTabAdapter tabAdapter;
    private FrameLayout fragmentContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.claim_tab_fragment, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        fragmentContainer = view.findViewById(R.id.fragment_container);

        setupTabs();
        loadInitialFragment();

        return view;
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("My Claims"));
        tabLayout.addTab(tabLayout.newTab().setText("Approvals"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadInitialFragment() {
        switchFragment(0); // Load the first tab's fragment by default
    }

    private void switchFragment(int position) {
        Fragment selectedFragment = null;

        switch (position) {
            case 0:
                selectedFragment = new ClaimSupervisorManagementFragment(); // Replace with your fragment class
                break;
            case 1:
                selectedFragment = new ClaimSupervisorApproveFragment(); // Replace with your fragment class
                break;
        }

        if (selectedFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
    }
}

