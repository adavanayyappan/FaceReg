package com.bestlabs.facerecoginination.ui.claim;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ClaimTabAdapter  extends FragmentStateAdapter {

    public ClaimTabAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ClaimSupervisorManagementFragment();
            default:
                return new ClaimSupervisorApproveFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}

