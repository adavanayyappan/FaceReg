package com.bestlabs.facerecoginination.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SharedPref;

public class SplashActivity extends AppCompatActivity {
    private Context context;
    private static final int REQUEST_CODE_LOCATION = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;


    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(PreferenceManager.getBoolean(context, Constants.KEY_ISLOGGEDIN, false)) {
//                    //if already login then open the dashboard
//                    //check if it is supervisor or worker
//                    if(false) {
//                        Intent intent = new Intent(context,SupervisorDashboard.class);
//                        finish();
//                        startActivity(intent);
//                    }
//                    else {
//                        Intent intent = new Intent(context,WorkerDashboardActivity.class);
//                        finish();
//                        startActivity(intent);
//                    }
//                }
//                else {
//                    Intent intent = new Intent(context,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
                if (checkLocationPermission()) {
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, "Location permission denied. Please grant permission", Toast.LENGTH_SHORT).show();
                }
            }
        },1500);
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_LOCATION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Location permission denied. Please grant permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
