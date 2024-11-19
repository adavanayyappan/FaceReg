package com.bestlabs.facerecoginination.ui.claim;

import android.Manifest;
import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.bestlabs.facerecoginination.adapters.ClaimTypeSelectedListRVAdapter;
import com.bestlabs.facerecoginination.adapters.LeaveTypeSelectedListRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimGroupModel;
import com.bestlabs.facerecoginination.models.LeaveApplyResponse;
import com.bestlabs.facerecoginination.models.LeaveListModel;
import com.bestlabs.facerecoginination.others.AlertDialogHelper;
import com.bestlabs.facerecoginination.others.Base64Utils;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.ImageUtils;
import com.bestlabs.facerecoginination.others.NetworkUtils;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.bestlabs.facerecoginination.ui.leave_management.LeaveWorkerApplyActivity;
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

public class ClaimWorkerApplyActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private AppCompatButton applyAllowance_Btn;
    private TextInputLayout textInputLayoutAppliedDate, textInputLayoutDescription, textInputLayoutClaim;
    private TextInputEditText editTextAppliedDate, editTextDescription, editTextClaim;
    private Button plusButtonAttachment;
    private ImageView iVAttachment;
    private TextView tvAttachmentName;
    private AlertDialog dialog;
    APIInterface apiService;
    private ClaimTypeSelectedListRVAdapter claimTypeSelectedListRVAdapter;
    static RecyclerView recyclerViewClaim;
    private Bitmap selectedBitap;
    private Integer claimRequestID = 0;

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_apply_allowance);

        setUpToolBar();
        tvAttachmentName = findViewById(R.id.tvAttachmentName);
        constraintLayout = findViewById(R.id.constraint_layout);
        textInputLayoutAppliedDate = findViewById(R.id.textInputAppliedDate);
        textInputLayoutDescription = findViewById(R.id.textInputDescription);
        textInputLayoutClaim = findViewById(R.id.textInputAppliedAmount);

        editTextAppliedDate = findViewById(R.id.editTextAppliedDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextClaim = findViewById(R.id.editTextClaimAmount);

        plusButtonAttachment = findViewById(R.id.plusButtonAttachment);
        applyAllowance_Btn = findViewById(R.id.applyClaimButton);

        iVAttachment = findViewById(R.id.iVAttachment);
        tvAttachmentName = findViewById(R.id.tvAttachmentName);

        setCurrentDate();
        tvAttachmentName.setVisibility(View.GONE);
        iVAttachment.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(ClaimWorkerApplyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(ClaimWorkerApplyActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {

        }

        // Initialize the file picker launcher
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedFileUri = data.getData(); // Extract the Uri
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
                }
        );

        plusButtonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if permission is not granted
                if (ContextCompat.checkSelfPermission(ClaimWorkerApplyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(ClaimWorkerApplyActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    // Permission already granted, proceed with your logic
                    // Launch file picker intent
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    filePickerLauncher.launch(intent);
                }
            }
        });

        applyAllowance_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    // Perform your action if all fields are valid
                    applyLeaveRequest();
                } else {
                    // Show an error message or handle invalid fields
                    AlertDialogHelper.showSnackbar(constraintLayout,"Please enter valid details." );
                }
            }
        });

        ProgressBar progressBar = new ProgressBar(ClaimWorkerApplyActivity.this);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClaimWorkerApplyActivity.this);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);

        recyclerViewClaim = findViewById(R.id.rc_worker_claim_list);

        apiService = APIClient.getClient().create(APIInterface.class);
        Intent mIntent = getIntent();
        claimRequestID = mIntent.getIntExtra("claimRequestID", 0);
        getClaimListTypeData();
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

            if (data != null) {
                Uri uri = data.getData(); // Extract the Uri
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
        String description = editTextDescription.getText().toString().trim();
        String claim = editTextClaim.getText().toString().trim();
        // Additional validations for other fields
        if (TextUtils.isEmpty(description)) {
            textInputLayoutDescription.setError("Description is required");
        } else {
            textInputLayoutDescription.setError(null);
        }

        // Additional validations for other fields
        if (TextUtils.isEmpty(claim)) {
            textInputLayoutClaim.setError("Claim Amount is required");
        } else {
            textInputLayoutClaim.setError(null);
        }

        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(claim)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getClaimListTypeData() {
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
            Call<ClaimGroupModel> call = apiService.getClaimList(token, empID_STR, clientID_STR, "dropdown");

            call.enqueue(new Callback<ClaimGroupModel>() {
                @Override
                public void onResponse(Call<ClaimGroupModel> call, Response<ClaimGroupModel> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        ClaimGroupModel claimGroupModel = response.body();
                        if (claimGroupModel.getResult() == null) {
                            return;
                        }
                        int selectedPosition = RecyclerView.NO_POSITION;
                        for (int i = 0; i < claimGroupModel.getResult().size(); i++) {
                            if (claimGroupModel.getResult().get(i).getValue() == claimRequestID) {
                                selectedPosition = i;
                            }
                        }
                        claimTypeSelectedListRVAdapter = new ClaimTypeSelectedListRVAdapter((ArrayList<ClaimGroupModel.Result>) claimGroupModel.getResult(), ClaimWorkerApplyActivity.this, selectedPosition, new ClaimTypeSelectedListRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                claimRequestID = position;
                            }
                        });
                        recyclerViewClaim.setNestedScrollingEnabled(false);
                        recyclerViewClaim.setAdapter(claimTypeSelectedListRVAdapter);
                        recyclerViewClaim.setLayoutManager(new LinearLayoutManager(ClaimWorkerApplyActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    } else {
                        // Handle error response
                        dialog.dismiss();
                        AlertDialogHelper.showSnackbar(constraintLayout,"Oops Failed. Please Try Again" );
                    }
                }

                @Override
                public void onFailure(Call<ClaimGroupModel> call, Throwable t) {
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
                    getClaimListTypeData();
                }
            });
        }

    }

    public int getIndexByValue(List<String> list, Integer value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                return i; // Return the index if value matches
            }
        }
        return -1; // Return -1 if value is not found
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

        MultipartBody.Part body = null;

        if (selectedBitap != null) {
            File file = ImageUtils.convertImageViewToFile(ClaimWorkerApplyActivity.this, selectedBitap);

            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/jpg"),
                            file
                    );
            body = MultipartBody.Part.createFormData("claimDoc", file.getName(), requestFile);
        }

        String claimAmount = editTextClaim.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();


        RequestBody empIDValue = RequestBody.create(MediaType.parse("text/plain"), empID_STR);
        RequestBody clientIDValue = RequestBody.create(MediaType.parse("text/plain"), clientID_STR);
        RequestBody claimTypeID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(claimRequestID));
        RequestBody claimAmountValue = RequestBody.create(MediaType.parse("text/plain"), claimAmount);
        RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), description);

        Call<LeaveApplyResponse> call = apiService.applyClaim(token, body, empIDValue, clientIDValue, claimTypeID, claimAmountValue, remarks);

        call.enqueue(new Callback<LeaveApplyResponse>() {
            @Override
            public void onResponse(Call<LeaveApplyResponse> call, Response<LeaveApplyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    LeaveApplyResponse addResponse = response.body();
                    Log.e("Response", ""+response.body());
                    Log.e("Status", addResponse.getStatus());

                    dialog.dismiss();
                    if (addResponse.getStatus().equals("success")) {
                        finish();
                    } else {
                        AlertDialogHelper.showSnackbar(constraintLayout, "Apply claim Failed. Please Try Again");
                    }
                } else {
                    // Handle unsuccessful response
                    // Example of using the AlertDialogHelper to show an alert dialog
                    dialog.dismiss();
                    AlertDialogHelper.showSnackbar(constraintLayout, "Apply claim Failed. Please Try Again");
                }
            }

            @Override
            public void onFailure(Call<LeaveApplyResponse> call, Throwable t) {
                // Handle failure
                dialog.dismiss();
                AlertDialogHelper.showSnackbar(constraintLayout, "Apply claim Failed. Please Try Again");
            }
        });
    }
}


