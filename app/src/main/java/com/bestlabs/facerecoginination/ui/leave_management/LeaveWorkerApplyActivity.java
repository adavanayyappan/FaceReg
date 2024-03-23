package com.bestlabs.facerecoginination.ui.leave_management;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bestlabs.facerecoginination.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LeaveWorkerApplyActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private CardView totalLeaveCard, annualLeaveCard, casualLeaveCard, sickLeaveCard;
    private TextView totalAppliedTv, annualAppliedTv, casualAppliedTv, sickAppliedTv;
    private TextView totalAvailableTv, annualAvailableTv, casualAvailableTv, sickAvailableTv;
    private AppCompatButton applyLeave_Btn;
    private TextInputLayout textInputLayoutAppliedDate, textInputLayoutStartDate, textInputLayoutEndDate, textInputLayoutDescription;
    private TextInputEditText editTextAppliedDate, editTextStartDate, editTextEndDate, editTextDescription;
    private Spinner spinnerDays;
    private Button plusButtonAttachment;
    private ImageView iVAttachment;
    private TextView tvAttachmentName;

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_apply_leave);

        setUpToolBar();
        totalLeaveCard = findViewById(R.id.card_lay_total);
        annualLeaveCard = findViewById(R.id.card_lay_annual);
        casualLeaveCard = findViewById(R.id.card_lay_casual);
        sickLeaveCard = findViewById(R.id.card_lay_sick);
        applyLeave_Btn = findViewById(R.id.fab_apply_leave);

        totalAppliedTv = findViewById(R.id.tv_tot_applied_leave);
        annualAppliedTv = findViewById(R.id.tv_annual_applied_leave);
        casualAppliedTv = findViewById(R.id.tv_casual_applied_leave);
        sickAppliedTv = findViewById(R.id.tv_sick_applied_leave);

        totalAvailableTv = findViewById(R.id.tv_tot_remaining_leave);
        annualAvailableTv = findViewById(R.id.tv_annual_remaining_leave);
        casualAvailableTv = findViewById(R.id.tv_casual_remaining_leave);
        sickAvailableTv = findViewById(R.id.tv_sick_remaining_leave);

        tvAttachmentName = findViewById(R.id.tvAttachmentName);

        textInputLayoutAppliedDate = findViewById(R.id.textInputAppliedDate);
        textInputLayoutStartDate = findViewById(R.id.textInputStartDate);
        textInputLayoutEndDate = findViewById(R.id.textInputEndDate);
        textInputLayoutDescription = findViewById(R.id.textInputDescription);

        editTextAppliedDate = findViewById(R.id.editTextAppliedDate);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextDescription = findViewById(R.id.editTextDescription);

        spinnerDays = findViewById(R.id.spinnerDays);
        plusButtonAttachment = findViewById(R.id.plusButtonAttachment);
        applyLeave_Btn = findViewById(R.id.applyLeaveButton);

        iVAttachment = findViewById(R.id.iVAttachment);
        tvAttachmentName = findViewById(R.id.tvAttachmentName);

        setCurrentDate();
        tvAttachmentName.setVisibility(View.GONE);
        iVAttachment.setVisibility(View.GONE);

        // Create a list of numbers from 1 to 50
        List<String> numbersList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            numbersList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numbersList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerDays.setAdapter(adapter);

        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(true);
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(false);
            }
        });

        // Set OnClickListener for totalLeaveCard
        totalLeaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for totalLeaveCard
                // Example: Launch a new activity, show a dialog, etc.
            }
        });

        // Set OnClickListener for annualLeaveCard
        annualLeaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for annualLeaveCard
            }
        });

        // Set OnClickListener for casualLeaveCard
        casualLeaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for casualLeaveCard
            }
        });

        // Set OnClickListener for sickLeaveCard
        sickLeaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for sickLeaveCard
            }
        });

        plusButtonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch file picker intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Allow all file types
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });

        applyLeave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    // Perform your action if all fields are valid
                } else {
                    // Show an error message or handle invalid fields
                }
            }
        });
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int titleTextColor = Color.WHITE; // Change it to the color you desire
        toolbar.setTitleTextColor(titleTextColor);
        setTitle("Apply Leave");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back); // Replace ic_arrow_back with your back arrow icon
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String dateFormat = "dd-MMM-yyyy";
        CharSequence currentDate = DateFormat.format(dateFormat, calendar);
        editTextAppliedDate.setText(currentDate);
        editTextAppliedDate.setKeyListener(null);
        editTextStartDate.setKeyListener(null);
        editTextEndDate.setKeyListener(null);
    }

    private void showDatePickerDialog(Boolean isStartDate) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the date picker's initial values
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Update the EditText with the selected date
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + selectedYear;
                        if (isStartDate == true) {
                            editTextStartDate.setText(selectedDate);
                        } else {
                            editTextEndDate.setText(selectedDate);
                        }
                    }
                },
                year,
                month,
                day
        );

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the selected file URI
            Uri selectedFileUri = data.getData();

            // Get the filename from the URI
            String selectedFileName = getFileName(selectedFileUri);

            // Display the selected filename in the TextView
            tvAttachmentName.setText(selectedFileName);
            tvAttachmentName.setVisibility(View.VISIBLE);
            iVAttachment.setVisibility(View.VISIBLE);
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(nameIndex);
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private boolean validateFields() {
        String appliedDateString = editTextAppliedDate.getText().toString().trim();
        String startDateString = editTextStartDate.getText().toString().trim();
        String endDateString = editTextEndDate.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int selectedDays = (int) spinnerDays.getSelectedItem();

        try {
            Date appliedDate = dateFormat.parse(appliedDateString);
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            // Check if end date is greater than start date
            if (endDate.before(startDate)) {
                editTextEndDate.setError("End date must be after start date");
                return false;
            }

            // Additional validations for other fields
            if (TextUtils.isEmpty(description)) {
                editTextDescription.setError("Description is required");
                return false;
            }

            // Perform additional validations as needed

            // If all validations pass
            return true;

        } catch (ParseException e) {
            // Handle ParseException (invalid date format)
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

