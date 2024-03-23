package com.bestlabs.facerecoginination.NetworkManager;

import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.models.TimeSheetModel;
import com.bestlabs.facerecoginination.models.UserModel;
import com.bestlabs.facerecoginination.models.UserProfile;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("claims")
    Call<List<ClaimModel>> getClaims();

    @GET("leaveData") // Replace with your actual endpoint
    Call<List<LeaveModel>> getLeaveData();

    @GET("leaveData") // Replace with your actual endpoint
    Call<List<TimeSheetModel>> getTimeSheetData();

    @PUT("updateProfile")
    Call<UserModel> updateProfile(@Body UserProfile userProfile);

    @Multipart
    @POST("uploadImage")
    Call<String> uploadImage(
            @Part MultipartBody.Part image,
            @Part("description") RequestBody description
    );
}