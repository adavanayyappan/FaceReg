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
import com.bestlabs.facerecoginination.models.ForgotPasswordModel;
import com.bestlabs.facerecoginination.models.LoginUserModel;
import com.bestlabs.facerecoginination.models.ResetPasswordModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity  extends AppCompatActivity {
    EditText mEmail, motp;
    EditText passWd, confirmPassWd;
    Button mSubmitBtn;
    private AlertDialog dialog;
    Context context;
    private ConstraintLayout constraintLayout;
    String recivedOtp;

    private APIInterface apiService;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        apiService = APIClient.getClient().create(APIInterface.class);

        Intent intent = getIntent();

        // Retrieve the string using the key you used to put the extra
        recivedOtp = intent.getStringExtra("temp_otp");

        initialize();
        clickListners();
    }

    private void initialize() {
        context = ResetPasswordActivity.this;
        mEmail = findViewById(R.id.edt_email);
        motp = findViewById(R.id.edt_otp);
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
                String email = mEmail.getText().toString();
                String otp = motp.getText().toString();
                if (email.isEmpty()) {
                    mEmail.setError("Please enter email");
                    return;
                }

                if (!isValidEmail(email)) {
                    mEmail.setError("Invalid Email");
                    return;
                }

                if (otp.isEmpty()) {
                    mEmail.setError("Please enter otp");
                    return;
                }

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

                if (!otp.equals(recivedOtp)) {
                    mEmail.setError("Please enter valid otp");
                    return;
                }


                if (email.isEmpty() || otp.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                    Toast.makeText(ResetPasswordActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                else {
                    postForgotPasswordRequest(email, otp, password, confirmPassword);
                }
            }
        });
    }

    // Method to make the POST request
    public void postForgotPasswordRequest(String userName, String Otp, String password, String cpassword) {
        dialog.show();
        Call<ResetPasswordModel> call = apiService.resetPassword(userName, Otp, password, cpassword);

        call.enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(Call<ResetPasswordModel> call, Response<ResetPasswordModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    Log.e("response", ""+response.body());
                    dialog.dismiss();
                    ResetPasswordModel result = response.body();
                    if (result.getStatus().equals("success")) {
                        Toast.makeText(ResetPasswordActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                        finish();
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
            public void onFailure(Call<ResetPasswordModel> call, Throwable t) {
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
}


