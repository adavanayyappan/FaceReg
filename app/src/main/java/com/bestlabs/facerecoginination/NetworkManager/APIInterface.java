package com.bestlabs.facerecoginination.NetworkManager;

import com.bestlabs.facerecoginination.models.ClaimGroupModel;
import com.bestlabs.facerecoginination.models.ClaimModel;
import com.bestlabs.facerecoginination.models.ClaimRequestListResponse;
import com.bestlabs.facerecoginination.models.ClaimUpdateStatus;
import com.bestlabs.facerecoginination.models.FaceAddResponse;
import com.bestlabs.facerecoginination.models.ForgotPasswordModel;
import com.bestlabs.facerecoginination.models.LeaveApplyResponse;
import com.bestlabs.facerecoginination.models.LeaveCategoryModel;
import com.bestlabs.facerecoginination.models.LeaveListModel;
import com.bestlabs.facerecoginination.models.LeaveModel;
import com.bestlabs.facerecoginination.models.LeaveRequestListResponse;
import com.bestlabs.facerecoginination.models.LeaveUpdateStatus;
import com.bestlabs.facerecoginination.models.LoginUserModel;
import com.bestlabs.facerecoginination.models.PunchListModel;
import com.bestlabs.facerecoginination.models.PunchModel;
import com.bestlabs.facerecoginination.models.PunchStatusModel;
import com.bestlabs.facerecoginination.models.ResetPasswordModel;
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
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("users/login")
    Call<LoginUserModel> postLogin(
            @Field("userName") String userName,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/forgotpassword")
    Call<ForgotPasswordModel> forgotPassword(
            @Field("empEmailId") String empID
    );

    @FormUrlEncoded
    @POST("users/resetpassword")
    Call<ResetPasswordModel> resetPassword(
            @Field("empEmailId") String empID,
            @Field("OTPKey") String OTPKey,
            @Field("password") String password,
            @Field("cpassword") String cpassword
    );

    @FormUrlEncoded
    @POST("users/changepassword")
    Call<LeaveApplyResponse> changePassword(
            @Header("Authorization") String authToken,
            @Field("empID") String empID,
            @Field("clientID") String clientID,
            @Field("password") String password,
            @Field("cpassword") String cpassword
    );

    @FormUrlEncoded
    @POST("attendance/punching")
    Call<PunchModel> postPunching(
            @Header("Authorization") String authToken,
            @Field("empID") String empID,
            @Field("clientID") String clientID,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @GET("attendance/punchingstatus")
    Call<PunchStatusModel> getPunchStatus(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @GET("attendance/punchinglist")
    Call<PunchListModel> getPunchList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID,
            @Query("month") String month
    );

    @GET("leaveType/dropdownlist")
    Call<LeaveCategoryModel> getLeaveCategoryList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @Multipart
    @POST("attendance/addFace")
    Call<FaceAddResponse> uploadAddFace(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part file,
            @Part("empID") RequestBody empID,
            @Part("clientID") RequestBody clientID
    );

    @GET("Leave/leavelist")
    Call<LeaveListModel> getLeaveList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @Multipart
    @POST("Leave/apply")
    Call<LeaveApplyResponse> applyLeave(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part file,
            @Part("empID") RequestBody empID,
            @Part("clientID") RequestBody clientID,
            @Part("leaveTypeID") RequestBody leaveTypeID,
            @Part("fromDate") RequestBody fromDate,
            @Part("toDate") RequestBody toDate,
            @Part("remarks") RequestBody remarks,
            @Part("coWorkerEmailID") RequestBody coWorkerEmailID
    );

    @GET("Leave/requestlist")
    Call<LeaveRequestListResponse> getRequestLeaveList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @GET("Leave/myapproval")
    Call<LeaveRequestListResponse> getApprovalLeaveList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @FormUrlEncoded
    @POST("Leave/updatestatus")
    Call<LeaveUpdateStatus> postLeaveStatus(
            @Header("Authorization") String authToken,
            @Field("empID") String empID,
            @Field("clientID") String clientID,
            @Field("leaveID") Integer leaveID,
            @Field("actionType") String actionType,
            @Field("remarks") String remarks
    );

    @GET("claim/claimgroup")
    Call<ClaimGroupModel> getClaimList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID,
            @Query("viewtype") String viewtype
    );

    @Multipart
    @POST("claim/apply")
    Call<LeaveApplyResponse> applyClaim(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part file,
            @Part("empID") RequestBody empID,
            @Part("clientID") RequestBody clientID,
            @Part("claimGroupID") RequestBody leaveTypeID,
            @Part("claimAmount") RequestBody fromDate,
            @Part("remarks") RequestBody remarks
    );

    @GET("claim/requestlist")
    Call<ClaimRequestListResponse> getRequestClaimList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @GET("claim/myapproval")
    Call<ClaimRequestListResponse> getApprovalClaimList(
            @Header("Authorization") String authToken,
            @Query("empID") String empID,
            @Query("clientID") String clientID
    );

    @FormUrlEncoded
    @POST("claim/updatestatus")
    Call<ClaimUpdateStatus> postClaimStatus(
            @Header("Authorization") String authToken,
            @Field("empID") String empID,
            @Field("clientID") String clientID,
            @Field("ClaimID") Integer leaveID,
            @Field("actionType") String actionType,
            @Field("remarks") String remarks
    );


    @PUT("updateProfile")
    Call<UserModel> updateProfile(@Body UserProfile userProfile);
}