package com.bestlabs.facerecoginination.activities;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.LoginUserModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.others.SharedPref;
import com.bestlabs.facerecoginination.others.SimilarityClassifier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView txtFrgtPwd, txtSignup;
    private  AlertDialog dialog;
    Context context;
    private Boolean isSwipe = false;
    private ConstraintLayout constraintLayout;

    private static final int REQUEST_CODE_LOCATION = 1001;

    private APIInterface apiService;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        apiService = APIClient.getClient().create(APIInterface.class);

        initialize();
        clickListners();
    }

    private void initialize() {
        context = LoginActivity.this;
        mEmail = findViewById(R.id.edt_email);
        mPassword = findViewById(R.id.edt_password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        txtFrgtPwd = findViewById(R.id.tv_forgot_pwd);
        txtSignup = findViewById(R.id.tv_signup);
        txtSignup.setVisibility(View.GONE);
        constraintLayout = findViewById(R.id.constraint_layout);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        mEmail.setText("98765432");
        mPassword.setText("Password@123");
    }

    private void clickListners(){
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (email.isEmpty()) {
                    mEmail.setError("Please enter email/ mobile number");
                }

                if (password.isEmpty()) {
                    mPassword.setError("Please enter password");
                    return;
                }

                if (!isValidPassword(password)) {
                    mPassword.setError("Invalid Password");
                    return;
                }
                if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                else {
                     if (!checkLocationPermission()) {
                         AlertDialogHelper.showSnackbar(constraintLayout,"Location permission denied. Please grant permission" );
                     }
                    // Check if the internet is available
                    if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                        // Your network-related logic here
                        postLoginDataRequest(email, password);
                    } else {
                        // Display Snackbar with retry option
                        NetworkUtils.showNoInternetSnackbar(constraintLayout, new NetworkUtils.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                // Handle retry action
                                // You may want to reattempt the network operation
                            }
                        });
                    }
                }
            }
        });
        txtFrgtPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    // Method to make the POST request
    public void postLoginDataRequest(String userName, String password) {
        dialog.show();
        Call<LoginUserModel> call = apiService.postLogin(userName, password);

        call.enqueue(new Callback<LoginUserModel>() {
            @Override
            public void onResponse(Call<LoginUserModel> call, Response<LoginUserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    Log.e("response", ""+response.body());
                    dialog.dismiss();
                    LoginUserModel result = response.body();
                    if (result.getStatus().equals("ok")) {
                        PreferenceManager.saveString(context, Constants.KEY_TOKEN, result.getResult().getToken());
                        PreferenceManager.saveInt(context, Constants.KEY_EMP_ID, result.getResult().getEmployeeID());
                        PreferenceManager.saveInt(context, Constants.KEY_CLIENT_ID, result.getResult().getEmployeeClientID());
                        PreferenceManager.saveString(context, Constants.KEY_NAME, result.getResult().getEmployeeName());
                        PreferenceManager.saveBoolean(context, Constants.KEY_ISLOGGEDIN, true);
                        PreferenceManager.saveString(context, Constants.KEY_EMP_IMAGE, result.getResult().getFaceImage());

                        if (!result.getResult().getAllowApprove().equals("No")) {
                            Intent intent = new Intent(getBaseContext(), WorkerDashboardActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getBaseContext(), SupervisorDashboard.class);
                            finish();
                            startActivity(intent);
                        }
                    } else {
                        AlertDialogHelper.showSnackbar(constraintLayout,"Login Failed. Please Try Again" );
                    }
                } else {
                    // Handle unsuccessful response
                    // Example of using the AlertDialogHelper to show an alert dialog
                    Log.e("response", ""+response.errorBody());
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout,"Login Failed. Please Try Again" );
                }
            }

            @Override
            public void onFailure(Call<LoginUserModel> call, Throwable t) {
                // Handle failure
                Log.e("response", ""+t.getLocalizedMessage());
                dialog.dismiss();
                AlertDialogHelper.showSnackbar(constraintLayout,"Login Failed. Please Try Again" );
            }
        });
    }


    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = p.matcher(email);
        return matcher.matches();
    }

    // Here is validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationPermission()) {
            startLocationUpdates();
        } else {
            AlertDialogHelper.showSnackbar(constraintLayout,"Location permission denied. Please grant permission" );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    private void stopLocationUpdates() {

    }
}

