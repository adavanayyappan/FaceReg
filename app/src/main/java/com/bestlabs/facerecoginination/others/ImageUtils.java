package com.bestlabs.facerecoginination.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {

    // Method to convert ImageView to File
    public static File convertImageViewToFile(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            File file = createTempFile(context);
            saveBitmapToFile(bitmap, file);
            return file;
        }
        return null;
    }

    // Method to retrieve Bitmap from ImageView
    private static Bitmap getBitmapFromImageView(ImageView imageView) {
        if (imageView != null && imageView.getDrawable() != null) {
            return BitmapFactory.decodeResource(imageView.getResources(),
                    imageView.getDrawable().getConstantState().hashCode());
        }
        return null;
    }

    // Method to create a temporary file
    private static File createTempFile(Context context) {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile("temp_image" +RandomTextGenerator.getRandomNumber(5), ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to save Bitmap to File
    private static void saveBitmapToFile(Bitmap bitmap, File file) {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
