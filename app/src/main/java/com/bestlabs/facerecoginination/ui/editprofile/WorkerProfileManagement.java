package com.bestlabs.facerecoginination.ui.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bestlabs.facerecoginination.R;
import com.bestlabs.facerecoginination.others.Constants;
import com.bestlabs.facerecoginination.others.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class WorkerProfileManagement extends Fragment {

    private ConstraintLayout constraintLayout;
    private CardView editImageCardView;
    private ImageView editImage;
    private TextView tvEmpName, tvDesignation;
    private AppCompatButton save_Btn;
    private TextInputLayout textInputLayoutName, textInputLayoutDesignation, textInputLayoutEmail, textInputLayoutMobile, textInputLayoutGender, textInputLayoutDOB;
    private TextInputEditText textEditTextName, textEditTextDesignation, textEditTextEmail, textEditTextMobile, textEditTextGender, textEditTextDOB;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editImageCardView = root.findViewById(R.id.cardview_image_edit);
        editImage = root.findViewById(R.id.img_profile_edit);
        tvEmpName = root.findViewById(R.id.tv_emp_name);
        tvDesignation = root.findViewById(R.id.tv_emp_designation);
        save_Btn = root.findViewById(R.id.btn_save);
        textInputLayoutName = root.findViewById(R.id.textInputName);
        textInputLayoutDesignation = root.findViewById(R.id.textInputDesignation);
        textInputLayoutEmail = root.findViewById(R.id.textInputEmail);
        textInputLayoutMobile = root.findViewById(R.id.textInputMobile);
        textInputLayoutGender = root.findViewById(R.id.textInputGeneder);
        textInputLayoutDOB = root.findViewById(R.id.textInputDOB);
        textEditTextName = root.findViewById(R.id.editTextName);
        textEditTextDesignation = root.findViewById(R.id.editTextDesignation);
        textEditTextEmail = root.findViewById(R.id.editTextEmail);
        textEditTextMobile = root.findViewById(R.id.editTextMobile);
        textEditTextGender = root.findViewById(R.id.editTextGender);
        textEditTextDOB = root.findViewById(R.id.editTextDOB);

        String image_prefix = PreferenceManager.getString(getActivity(), Constants.KEY_EMP_IMAGE, "");
        String emp_name = PreferenceManager.getString(getActivity(), Constants.KEY_NAME, "");

        Picasso.get().load(Constants.KEY_IMAGE_URL+image_prefix).into(editImage);
        tvEmpName.setText(emp_name);

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

        return root;
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

}

