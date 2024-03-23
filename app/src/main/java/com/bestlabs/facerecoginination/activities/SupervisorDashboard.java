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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.others.SharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int titleTextColor = Color.WHITE; // Change it to the color you desire
        toolbar.setTitleTextColor(titleTextColor);

        taskSortBundle = new Bundle();
        sharedPref = new SharedPref(SupervisorDashboard.this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setAnimation(null);

        //setting nav header items
        View header = navigationView.getHeaderView(0);
        nav_name = header.findViewById(R.id.nav_name);
        nav_email = header.findViewById(R.id.nav_email);
        nav_name.setText(sharedPref.getNAME());
        nav_email.setText(sharedPref.getEMAIL());
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisorDashboard.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        mProfileImage = header.findViewById(R.id.profile_image);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_terms,
                R.id.nav_privacy, R.id.nav_contact_us)
                .setDrawerLayout(drawer)
                .build();


        NavController bottomNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, bottomNavController);

        navigationView.setItemIconTintList(null);
        //setting icon tint to white for other menu items
        setDefaultIconTint();
        setTitle("Dashboard");
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


    private void setDefaultIconTint() {
        linearLayout = findViewById(R.id.ll_logout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref sharedPref = new SharedPref(getBaseContext());
                sharedPref.logout();
                ExitActivity.exitApplicationAndRemoveFromRecent(SupervisorDashboard.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.worker_dashboard, menu);
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
}
