package com.bestlabs.facerecoginination.activities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bestlabs.facerecoginination.others.SimilarityClassifier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class MyTask extends AsyncTask<String, Void, Bitmap> {

    private HashMap<String, SimilarityClassifier.Recognition> registered = new HashMap<>(); //saved Faces

    Context context;
    String name;
    String type;
    private AlertDialog dialog;

    public MyTask(Context context, String name, String type) {
        this.context = context;
        this.name = name;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);
        dialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        String imageUrl = params[0];
        try {
            InputStream inputStream = new java.net.URL(imageUrl).openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null ) {
            saveImageFromServer(result);
            // Save the bitmap
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if(type.equals("No")) {
                Intent intent = new Intent(context, WorkerDashboardActivity.class);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, SupervisorDashboard.class);
                context.startActivity(intent);
            }

        }
    }


    public void saveImageFromServer(final Bitmap bitmap) {
        int[] intValues;
        int inputSize = 112;  //Input size for model
        boolean isModelQuantized = false;
        float[][] embeedings;
        float IMAGE_MEAN = 128.0f;
        float IMAGE_STD = 128.0f;
        int OUTPUT_SIZE = 192; //Output size of model

        //Create ByteBuffer to store normalized image

        ByteBuffer imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4);

        imgData.order(ByteOrder.nativeOrder());

        intValues = new int[inputSize * inputSize];
        Log.e("bitmap", ""+bitmap);
        //get pixel values from Bitmap to normalize
//        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        imgData.rewind();

        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int pixelValue = intValues[i * inputSize + j];
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((byte) ((pixelValue >> 16) & 0xFF));
                    imgData.put((byte) ((pixelValue >> 8) & 0xFF));
                    imgData.put((byte) (pixelValue & 0xFF));
                } else { // Float model
                    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);

                }
            }
        }
        embeedings = new float[1][OUTPUT_SIZE];

        //Create and Initialize new object with Face embeddings and Name.
        SimilarityClassifier.Recognition result = new SimilarityClassifier.Recognition(
                "0", "", -1f);
        result.setExtra(embeedings);

        registered.put(name, result);

        insertToSP(registered);
    }

    private void insertToSP(HashMap<String, SimilarityClassifier.Recognition> jsonMap) {
        jsonMap.putAll(readFromSP());
        String jsonString = new Gson().toJson(jsonMap);
        SharedPreferences sharedPreferences = context.getSharedPreferences("HashMap", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("map", jsonString);
        editor.apply();
        Toast.makeText(context, "Recognitions Saved", Toast.LENGTH_SHORT).show();
    }

    //Load Faces from Shared Preferences.Json String to Recognition object
    private HashMap<String, SimilarityClassifier.Recognition> readFromSP() {
        int OUTPUT_SIZE = 192; //Output size of model
        SharedPreferences sharedPreferences = context.getSharedPreferences("HashMap", MODE_PRIVATE);
        String defValue = new Gson().toJson(new HashMap<String, SimilarityClassifier.Recognition>());
        String json = sharedPreferences.getString("map", defValue);
        // System.out.println("Output json"+json.toString());
        TypeToken<HashMap<String, SimilarityClassifier.Recognition>> token = new TypeToken<HashMap<String, SimilarityClassifier.Recognition>>() {
        };
        HashMap<String, SimilarityClassifier.Recognition> retrievedMap = new Gson().fromJson(json, token.getType());
        // System.out.println("Output map"+retrievedMap.toString());

        //During type conversion and save/load procedure,format changes(eg float converted to double).
        //So embeddings need to be extracted from it in required format(eg.double to float).
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : retrievedMap.entrySet()) {
            float[][] output = new float[1][OUTPUT_SIZE];
            ArrayList arrayList = (ArrayList) entry.getValue().getExtra();
            if (arrayList != null) {
                arrayList = (ArrayList) arrayList.get(0);
                for (int counter = 0; counter < arrayList.size(); counter++) {
                    output[0][counter] = ((Double) arrayList.get(counter)).floatValue();
                }
                entry.getValue().setExtra(output);
            }
        }
        Toast.makeText(context, "Recognitions Loaded", Toast.LENGTH_SHORT).show();
        return retrievedMap;
    }
}
