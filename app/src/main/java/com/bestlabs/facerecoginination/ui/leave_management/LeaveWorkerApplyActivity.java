package com.bestlabs.facerecoginination.ui.leave_management;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.activities.facereginationhelper.FaceAddTwoActivity;
import com.bestlabs.facerecoginination.adapters.LeaveTypeListRVAdapter;
import com.bestlabs.facerecoginination.adapters.TimeManagementAdapter;
import com.bestlabs.facerecoginination.models.FaceAddResponse;
import com.bestlabs.facerecoginination.models.LeaveCategoryModel;
import com.bestlabs.facerecoginination.models.LeaveListModel;
import com.bestlabs.facerecoginination.models.PunchListModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.ImageUtils;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveWorkerApplyActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private AppCompatButton applyLeave_Btn;
    private TextInputLayout textInputLayoutAppliedDate, textInputLayoutStartDate, textInputLayoutEndDate, textInputLayoutDescription;
    private TextInputEditText editTextAppliedDate, editTextStartDate, editTextEndDate, editTextDescription;
    private Spinner spinnerDays;
    private Button plusButtonAttachment;
    private ImageView iVAttachment;
    private TextView tvAttachmentName;
    private AlertDialog dialog;
    APIInterface apiService;
    private LeaveTypeListRVAdapter leaveTypeListRVAdapter;
    static RecyclerView recyclerViewLeave;
    private Bitmap selectedBitap;

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_apply_leave);

        setUpToolBar();
        constraintLayout = findViewById(R.id.constraint_layout);
        applyLeave_Btn = findViewById(R.id.fab_apply_leave);

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

        if (ContextCompat.checkSelfPermission(LeaveWorkerApplyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(LeaveWorkerApplyActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
           
        }

        ProgressBar progressBar = new ProgressBar(LeaveWorkerApplyActivity.this);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LeaveWorkerApplyActivity.this);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        recyclerViewLeave = findViewById(R.id.rc_worker_leave_list);

        apiService = APIClient.getClient().create(APIInterface.class);

        getLeaveTypeData();
        getLeaveListTypeData();

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


        plusButtonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if permission is not granted
                if (ContextCompat.checkSelfPermission(LeaveWorkerApplyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(LeaveWorkerApplyActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // Permission already granted, proceed with your logic
                    // Launch file picker intent
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
                }
            }
        });

        applyLeave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    applyLeaveRequest();
                } else {
                    // Show an error message or handle invalid fields
                    AlertDialogHelper.showSnackbar(constraintLayout,"Please enter valid details." );
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
        String dateFormat = "dd-MM-yyyy";
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

            try {
                // Convert the URI to a Bitmap
                selectedBitap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedFileUri);

            } catch (IOException e) {
                e.printStackTrace();
            }

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
        String selectedDays = (String) spinnerDays.getSelectedItem();

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

    private void getLeaveTypeData() {
        // Check if the internet is available
        if (NetworkUtils.isNetworkAvailable(LeaveWorkerApplyActivity.this)) {
            // Your network-related logic here
            dialog.show();
            String token = PreferenceManager.getString(this, Constants.KEY_TOKEN, "");
            int empID = PreferenceManager.getInt(this, Constants.KEY_EMP_ID, 0);
            int clientID = PreferenceManager.getInt(this, Constants.KEY_CLIENT_ID, 0);
            String empID_STR = Base64Utils.intToBase64(empID);
            String clientID_STR = Base64Utils.intToBase64(clientID);
            Log.e("token", ""+token);
            Log.e("empID_STR", ""+empID_STR);
            Log.e("clientID_STR", ""+clientID_STR);
            // Call the getPunchList method with authorization header and query parameters
            Call<LeaveCategoryModel> call = apiService.getLeaveCategoryList(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<LeaveCategoryModel>() {
                @Override
                public void onResponse(Call<LeaveCategoryModel> call, Response<LeaveCategoryModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        LeaveCategoryModel leaveCategoryModel = response.body();
                        Log.e("responseSize", ""+leaveCategoryModel.getResult().size());

                        // Create a list of numbers from 1 to 50
                        List<String> arraySpinner = new ArrayList<>();

                        for(int i = 0; i <= leaveCategoryModel.getResult().size() - 1; i++) {
                            arraySpinner.add(leaveCategoryModel.getResult().get(i).getLabel());
                        }

                        // Handle the response data
                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LeaveWorkerApplyActivity.this, android.R.layout.simple_spinner_item, arraySpinner);

                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Apply the adapter to the spinner
                        spinnerDays.setAdapter(adapter);
                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<LeaveCategoryModel> call, Throwable t) {
                    // Handle failure
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                }
            });
        } else {
            // Display Snackbar with retry option
            NetworkUtils.showNoInternetSnackbar(constraintLayout, new NetworkUtils.OnRetryListener() {
                @Override
                public void onRetry() {
                    // Handle retry action
                    getLeaveTypeData();
                }
            });
        }
    }

    private void getLeaveListTypeData() {
        // Check if the internet is available
        if (NetworkUtils.isNetworkAvailable(this)) {
            // Your network-related logic here
            dialog.show();
            String token = PreferenceManager.getString(this, Constants.KEY_TOKEN, "");
            int empID = PreferenceManager.getInt(this, Constants.KEY_EMP_ID, 0);
            int clientID = PreferenceManager.getInt(this, Constants.KEY_CLIENT_ID, 0);
            String empID_STR = Base64Utils.intToBase64(empID);
            String clientID_STR = Base64Utils.intToBase64(clientID);
            Log.e("token", ""+token);
            Log.e("empID_STR", ""+empID_STR);
            Log.e("clientID_STR", ""+clientID_STR);
            // Call the getPunchList method with authorization header and query parameters
            Call<LeaveListModel> call = apiService.getLeaveList(token, empID_STR, clientID_STR);

            call.enqueue(new Callback<LeaveListModel>() {
                @Override
                public void onResponse(Call<LeaveListModel> call, Response<LeaveListModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        LeaveListModel leaveListModels = response.body();

                        leaveTypeListRVAdapter = new LeaveTypeListRVAdapter((ArrayList<LeaveListModel.LeaveType>) leaveListModels.getResult(), LeaveWorkerApplyActivity.this);
                        recyclerViewLeave.setNestedScrollingEnabled(false);
                        recyclerViewLeave.setAdapter(leaveTypeListRVAdapter);
                        recyclerViewLeave.setLayoutManager(new LinearLayoutManager(LeaveWorkerApplyActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<LeaveListModel> call, Throwable t) {
                    // Handle failure
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                }
            });
        } else {
            // Display Snackbar with retry option
            NetworkUtils.showNoInternetSnackbar(constraintLayout, new NetworkUtils.OnRetryListener() {
                @Override
                public void onRetry() {
                    // Handle retry action
                    getLeaveListTypeData();
                }
            });
        }
    }

    // Method to make the POST request
    public void applyLeaveRequest() {
        dialog.show();
        String token = PreferenceManager.getString(this, Constants.KEY_TOKEN, "");
        int empID = PreferenceManager.getInt(this, Constants.KEY_EMP_ID, 0);
        int clientID = PreferenceManager.getInt(this, Constants.KEY_CLIENT_ID, 0);
        String empID_STR = Base64Utils.intToBase64(empID);
        String clientID_STR = Base64Utils.intToBase64(clientID);
        Log.e("token", ""+token);

        File file = ImageUtils.convertImageViewToFile(LeaveWorkerApplyActivity.this, selectedBitap);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("face_image", file.getName(), requestFile);

        String startDateString = editTextStartDate.getText().toString().trim();
        String endDateString = editTextEndDate.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        RequestBody empIDValue = RequestBody.create(MediaType.parse("text/plain"), empID_STR);
        RequestBody clientIDValue = RequestBody.create(MediaType.parse("text/plain"), clientID_STR);
        RequestBody leaveTypeID = RequestBody.create(MediaType.parse("text/plain"), empID_STR);
        RequestBody fromDate = RequestBody.create(MediaType.parse("text/plain"), startDateString);
        RequestBody toDate = RequestBody.create(MediaType.parse("text/plain"), endDateString);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody mailTo = RequestBody.create(MediaType.parse("text/plain"), "mailto:test3@gmail.com");

        Call<FaceAddResponse> call = apiService.applyLeave(token, body, empIDValue, clientIDValue, leaveTypeID, fromDate, toDate, remarks, mailTo);

        call.enqueue(new Callback<FaceAddResponse>() {
            @Override
            public void onResponse(Call<FaceAddResponse> call, Response<FaceAddResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    FaceAddResponse addResponse = response.body();
                    Log.e("Response", ""+response.body());
                    Log.e("Status", addResponse.getStatus());
                    Log.e("Image", addResponse.getFaceImage());

                    dialog.dismiss();
                    if (addResponse.getStatus().equals("success")) {

                        finish();
                    } else {
                        AlertDialogHelper.showSnackbar(constraintLayout, "Apply leave Failed. Please Try Again");
                    }
                } else {
                    // Handle unsuccessful response
                    // Example of using the AlertDialogHelper to show an alert dialog
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout, "Apply leave Failed. Please Try Again");
                }
            }

            @Override
            public void onFailure(Call<FaceAddResponse> call, Throwable t) {
                // Handle failure
                dialog.dismiss();
                AlertDialogHelper.showSnackbar(constraintLayout, "Apply leave Failed. Please Try Again");
            }
        });
    }
}

