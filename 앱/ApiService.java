package com.example.assertqr;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    final String baseURL="http://172.20.10.3:3000";

    @POST("/api/users")
    Call<JoinGet> join(@Body JoinPut data);

    @POST("/api/auth/login")
    Call<LoginGet> login(@Body LoginPut data);

    @GET("/api/qr")
    Call<QrGet> getQr(@Header("x-access-token") String token);
}
