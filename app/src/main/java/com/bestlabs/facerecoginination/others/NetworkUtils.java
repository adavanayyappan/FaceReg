package com.bestlabs.facerecoginination.others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;

public class NetworkUtils {

    // Check if the device is connected to the internet
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Display a Snackbar with a "No Internet" message and a retry action
    public static void showNoInternetSnackbar(@NonNull View view, @NonNull final OnRetryListener onRetryListener) {
        Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onRetryListener != null) {
                            onRetryListener.onRetry();
                        }
                    }
                })
                .show();
    }

    // Interface to handle retry action
    public interface OnRetryListener {
        void onRetry();
    }
}

