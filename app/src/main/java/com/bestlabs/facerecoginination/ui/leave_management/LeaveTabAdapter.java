package com.bestlabs.facerecoginination.ui.leave_management;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LeaveTabAdapter extends FragmentStateAdapter {

    public LeaveTabAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LeaveSupervisorManagementFragment();
            default:
                return new LeaveSupervisorApproveFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}

