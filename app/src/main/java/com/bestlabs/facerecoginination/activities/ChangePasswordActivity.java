package com.bestlabs.facerecoginination.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.models.LeaveApplyResponse;
import com.bestlabs.facerecoginination.models.LoginUserModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.ui.claim.ApproveClaimActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity   extends AppCompatActivity {
    EditText passWd, confirmPassWd;
    Button mSubmitBtn;
    private AlertDialog dialog;
    Context context;
    private ConstraintLayout constraintLayout;
    private APIInterface apiService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        apiService = APIClient.getClient().create(APIInterface.class);

        initialize();
        clickListners();
    }

    private void initialize() {
        context = ChangePasswordActivity.this;
        passWd = findViewById(R.id.edt_password);
        confirmPassWd = findViewById(R.id.edt_confirm_password);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        constraintLayout = findViewById(R.id.constraint_layout);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);
    }

    private void clickListners(){
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirmPassword = confirmPassWd.getText().toString();
                String password = passWd.getText().toString();

                if (password.isEmpty()) {
                    passWd.setError("Please enter password");
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    confirmPassWd.setError("Please enter confirm password");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    confirmPassWd.setError("Confirm password mismatch");
                    return;
                }

                if (!isValidPassword(password)) {
                    passWd.setError("Password should be greater than 6 characters");
                    return;
                }

                if (!isValidPassword(confirmPassword)) {
                    confirmPassWd.setError("Password should be greater than 6 characters");
                    return;
                }

                if (confirmPassword.isEmpty() || password.isEmpty())
                    Toast.makeText(ChangePasswordActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                else {
                    // Check if the internet is available
                    if (NetworkUtils.isNetworkAvailable(ChangePasswordActivity.this)) {
                        // Your network-related logic here
                        postChangePasswordRequest(password, confirmPassword);
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
    }


    // Method to make the POST request
    public void postChangePasswordRequest(String password, String cpassword) {
        dialog.show();

        String token = PreferenceManager.getString(ChangePasswordActivity.this, Constants.KEY_TOKEN, "");
        int empID = PreferenceManager.getInt(ChangePasswordActivity.this, Constants.KEY_EMP_ID, 0);
        int clientID = PreferenceManager.getInt(ChangePasswordActivity.this, Constants.KEY_CLIENT_ID, 0);
        String empID_STR = Base64Utils.intToBase64(empID);
        String clientID_STR = Base64Utils.intToBase64(clientID);
        Call<LeaveApplyResponse> call = apiService.changePassword(token,empID_STR, clientID_STR, password, cpassword);

        call.enqueue(new Callback<LeaveApplyResponse>() {
            @Override
            public void onResponse(Call<LeaveApplyResponse> call, Response<LeaveApplyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    Log.e("response", ""+response.body());
                    dialog.dismiss();
                    LeaveApplyResponse result = response.body();
                    if (result.getStatus().equals("success")) {
                        Toast.makeText(ChangePasswordActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        AlertDialogHelper.showSnackbar(constraintLayout,"Change Password Failed. Please Try Again" );
                    }
                } else {
                    // Handle unsuccessful response
                    // Example of using the AlertDialogHelper to show an alert dialog
                    Log.e("response", ""+response.errorBody());
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout,"Change Password Failed. Please Try Again" );
                }
            }

            @Override
            public void onFailure(Call<LeaveApplyResponse> call, Throwable t) {
                // Handle failure
                Log.e("response", ""+t.getLocalizedMessage());
                dialog.dismiss();
                AlertDialogHelper.showSnackbar(constraintLayout,"Change Password Failed. Please Try Again" );
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
}


