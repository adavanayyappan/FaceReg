package com.bestlabs.facerecoginination.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bestlabs.facerecoginination.NetworkManager.APIClient;
import com.bestlabs.facerecoginination.NetworkManager.APIInterface;
import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.adapters.ClaimManagementRVAdapter;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.UserModel;
import com.bestlabs.facerecoginination.models.UserProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity  extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private CardView editImageCardView;
    private ImageView editImage;
    private TextView tvEmpName, tvDesignation;
    private AppCompatButton save_Btn;
    private TextInputLayout textInputLayoutName, textInputLayoutDesignation, textInputLayoutEmail, textInputLayoutMobile, textInputLayoutGender, textInputLayoutDOB;
    private TextInputEditText textEditTextName, textEditTextDesignation, textEditTextEmail, textEditTextMobile, textEditTextGender, textEditTextDOB;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setUpToolBar();
        editImageCardView = findViewById(R.id.cardview_image_edit);
        editImage = findViewById(R.id.img_profile_edit);
        tvEmpName = findViewById(R.id.tv_emp_name);
        tvDesignation = findViewById(R.id.tv_emp_designation);
        save_Btn = findViewById(R.id.btn_save);
        textInputLayoutName = findViewById(R.id.textInputName);
        textInputLayoutDesignation = findViewById(R.id.textInputDesignation);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutMobile = findViewById(R.id.textInputMobile);
        textInputLayoutGender = findViewById(R.id.textInputGeneder);
        textInputLayoutDOB = findViewById(R.id.textInputDOB);
        textEditTextName = findViewById(R.id.editTextName);
        textEditTextDesignation = findViewById(R.id.editTextDesignation);
        textEditTextEmail = findViewById(R.id.editTextEmail);
        textEditTextMobile = findViewById(R.id.editTextMobile);
        textEditTextGender = findViewById(R.id.editTextGender);
        textEditTextDOB = findViewById(R.id.editTextDOB);

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateName() && validateEmail() && validateMobile() && validateGender() && validateDOB()) {
                    // Perform save operation or further processing
                }
            }
        });

        // Set onClickListener for the editImageCardView
        editImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a dialog or open gallery/camera based on your requirement
                openImageSelectionDialog();
            }
        });

    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int titleTextColor = Color.WHITE; // Change it to the color you desire
        toolbar.setTitleTextColor(titleTextColor);
        setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back); // Replace ic_arrow_back with your back arrow icon
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void updateProfile(UserProfile user) {
        // Create an instance of the Retrofit service
        APIInterface updateService = APIClient.getClient().create(APIInterface.class);

        Call<UserModel> call = updateService.updateProfile(user);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                } else {
                    // Handle error response
                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // Handle network failures or errors
            }
        });
    }

    private void openImageSelectionDialog() {
        // Implement your dialog or directly open gallery/camera based on user selection

        // Example: Open gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }


    private boolean validateName() {
        String name = textEditTextName.getText().toString().trim();
        if (name.isEmpty()) {
            textInputLayoutName.setError("Enter a valid name");
            return false;
        } else {
            textInputLayoutName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = textEditTextEmail.getText().toString().trim();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.setError("Enter a valid email address");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    private boolean validateMobile() {
        String mobile = textEditTextMobile.getText().toString().trim();
        if (mobile.isEmpty() || mobile.length() != 10) {
            textInputLayoutMobile.setError("Enter a valid 10-digit mobile number");
            return false;
        } else {
            textInputLayoutMobile.setError(null);
            return true;
        }
    }

    private boolean validateGender() {
        String gender = textEditTextGender.getText().toString().trim();
        if (gender.isEmpty()) {
            textInputLayoutGender.setError("Enter a valid gender");
            return false;
        } else {
            textInputLayoutGender.setError(null);
            return true;
        }
    }

    private boolean validateDOB() {
        String dob = textEditTextDOB.getText().toString().trim();
        // Add your logic for date of birth validation
        // Example: Check if it is in a valid format or within a certain range
        if (dob.isEmpty() || !isValidDOB(dob)) {
            textInputLayoutDOB.setError("Enter a valid date of birth");
            return false;
        } else {
            textInputLayoutDOB.setError(null);
            return true;
        }
    }

    private boolean isValidDOB(String dob) {
        // Add your logic to validate date of birth
        // Example: Check if it is in a valid format or within a certain range
        // Return true if valid, false otherwise
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // If an image was captured from the camera
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                editImage.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // If an image was selected from the gallery
                Uri selectedImageUri = data.getData();
                editImage.setImageURI(selectedImageUri);
            }
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
