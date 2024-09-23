package com.bestlabs.facerecoginination.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bestlabs.facerecoginination.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity  extends AppCompatActivity {
    EditText mEmail;
    Button mSubmitBtn;
    private AlertDialog dialog;
    Context context;
    private ConstraintLayout constraintLayout;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize();
        clickListners();
    }

    private void initialize() {
        context = ForgotPasswordActivity.this;
        mEmail = findViewById(R.id.edt_email);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
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
                if (email.isEmpty()) {
                    mEmail.setError("Please enter email/ mobile number");
                }

                if (!isValidEmail(email)) {
                    mEmail.setError("Invalid Email");
                }
                if (email.isEmpty())
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getBaseContext(), WorkerDashboardActivity.class);
                    finish();
                    startActivity(intent);
                }
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

