package com.bestlabs.facerecoginination.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class SupervisorDashboard extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView mProfileImage;
    private TextView mTvName,mTvEmail;
    private NavigationView navigationView;
    private Bundle taskSortBundle;
    private LinearLayout linearLayout;
    private SharedPref sharedPref;
    private TextView nav_name, nav_email;
    BottomNavigationView bottomNavigationView;
    private ConstraintLayout constraintLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        int titleTextColor = Color.WHITE; // Change it to the color you desire
        toolbar.setTitleTextColor(titleTextColor);
        setTitle("Dashboard");
        setSupportActionBar(toolbar);

        taskSortBundle = new Bundle();
        sharedPref = new SharedPref(SupervisorDashboard.this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle  = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);

        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setAnimation(null);
        constraintLayout = findViewById(R.id.container);
        String image_prefix = PreferenceManager.getString(this, Constants.KEY_EMP_IMAGE, "");
        String emp_name = PreferenceManager.getString(this, Constants.KEY_NAME, "");

        //setting nav header items
        View header = navigationView.getHeaderView(0);
        nav_name = header.findViewById(R.id.nav_name);
        nav_email = header.findViewById(R.id.nav_email);
        nav_email.setText(sharedPref.getEMAIL());
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisorDashboard.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        mProfileImage = header.findViewById(R.id.profile_image);
        Picasso.get().load(Constants.KEY_IMAGE_URL+image_prefix).into(mProfileImage);
        nav_name.setText(emp_name);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_terms,
                R.id.nav_privacy, R.id.nav_contact_us)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavController bottomNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, bottomNavController);

        navigationView.setItemIconTintList(null);
        //setting icon tint to white for other menu items
        setDefaultIconTint();
        // Set up a ColorStateList for icon colors
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };

        int[] colors = new int[]{
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.unselected_icon_color)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

        // Set the ColorStateList to the BottomNavigationView
        bottomNavigationView.setItemIconTintList(colorStateList);
        //setting custom icon tint to display project status
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_dashboard:
                    setTitle("Dashboard");
                    bottomNavController.navigate(R.id.nav_dashboard);
                    return true;
                case R.id.bottom_nav_calendar:
                    setTitle("TimeSheet");
                    bottomNavController.navigate(R.id.nav_timesheet);
                    return true;
                case R.id.bottom_nav_leave:
                    setTitle("Leave Summary");
                    bottomNavController.navigate(R.id.nav_leave);
                    return true;
                case R.id.bottom_nav_allowance:
                    setTitle("Claim Summary");
                    bottomNavController.navigate(R.id.nav_allowance);
                    return true;
                default:
                    return false;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();
        String image_prefix = PreferenceManager.getString(this, Constants.KEY_EMP_IMAGE, "");
        Picasso.get().load(Constants.KEY_IMAGE_URL+image_prefix).into(mProfileImage);
    }

    private void checkNetwork() {

        if (!NetworkUtils.isNetworkAvailable(this)) {
            // Display Snackbar with retry option
            NetworkUtils.showNoInternetSnackbar(constraintLayout, new NetworkUtils.OnRetryListener() {
                @Override
                public void onRetry() {
                    // Handle retry action
                    checkNetwork();
                }
            });
        }

    }


    private void setDefaultIconTint() {
        linearLayout = findViewById(R.id.ll_logout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref sharedPref = new SharedPref(getBaseContext());
                sharedPref.logout();
                ExitActivity.exitApplicationAndRemoveFromRecent(SupervisorDashboard.this);
                PreferenceManager.clearPreferences(SupervisorDashboard.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
