package com.bestlabs.facerecoginination.others;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

public class AlertDialogHelper {

    public static void showAlertDialog(Context context, String title, String message,
                                       String positiveButtonLabel, String negativeButtonLabel,
                                       DialogInterface.OnClickListener positiveClickListener,
                                       DialogInterface.OnClickListener negativeClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonLabel, positiveClickListener)
                .setNegativeButton(negativeButtonLabel, negativeClickListener)
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Display a Snackbar with a "No Internet" message and a retry action
    public static void showSnackbar(@NonNull View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }
}