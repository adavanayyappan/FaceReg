package com.bestlabs.facerecoginination.ui.claim;

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

public class ClaimWorkerApplyActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private AppCompatButton applyAllowance_Btn;
    private TextInputLayout textInputLayoutAppliedDate, textInputLayoutDescription;
    private TextInputEditText editTextAppliedDate, editTextDescription;
    private Spinner spinnerDays;
    private Button plusButtonAttachment;
    private ImageView iVAttachment;
    private TextView tvAttachmentName;

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_apply_allowance);

        setUpToolBar();
        tvAttachmentName = findViewById(R.id.tvAttachmentName);

        textInputLayoutAppliedDate = findViewById(R.id.textInputAppliedDate);
        textInputLayoutDescription = findViewById(R.id.textInputDescription);

        editTextAppliedDate = findViewById(R.id.editTextAppliedDate);
        editTextDescription = findViewById(R.id.editTextDescription);

        spinnerDays = findViewById(R.id.spinnerDays);
        plusButtonAttachment = findViewById(R.id.plusButtonAttachment);
        applyAllowance_Btn = findViewById(R.id.applyClaimButton);

        iVAttachment = findViewById(R.id.iVAttachment);
        tvAttachmentName = findViewById(R.id.tvAttachmentName);

        setCurrentDate();
        tvAttachmentName.setVisibility(View.GONE);
        iVAttachment.setVisibility(View.GONE);

        // Create a list of numbers from 1 to 50
        String[] arraySpinner = new String[] {"-- Select Allowance --","Food Allowance", "Travel Allowance", "Laptop", "Accommodation"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerDays.setAdapter(adapter);

        plusButtonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch file picker intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Allow all file types
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });

        applyAllowance_Btn.setOnClickListener(new View.OnClickListener() {
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
        setTitle("Apply Claim");
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
        String description = editTextDescription.getText().toString().trim();
        int selectedDays = (int) spinnerDays.getSelectedItem();

        try {
            Date appliedDate = dateFormat.parse(appliedDateString);

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


