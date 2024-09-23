package com.bestlabs.facerecoginination.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SharedPref;

public class SplashActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;

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
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}
