package com.edvaldotsi.nodejsandandroid.retrofit;

import com.edvaldotsi.nodejsandandroid.model.Auth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Edvaldo on 05/05/2016.
 */
public interface AuthService {

    @POST("/api/auth")
    @FormUrlEncoded
    Call<Auth> auth(
            @Field("email")
            String email,

            @Field("password")
            String password
    );

    @GET("/api/auth/validate")
    Call<Auth> validate(
            @Query("token")
            String token
    );
}
