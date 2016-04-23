package com.edvaldotsi.nodejsandandroid.retrofit;

import com.edvaldotsi.nodejsandandroid.model.User;
import com.edvaldotsi.nodejsandandroid.model.UserAuth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Edvaldo on 21/04/2016.
 */
public interface UserService {

    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("/api/auth")
    @FormUrlEncoded
    Call<UserAuth> auth(@Field("email") String email, @Field("password") String password);
}
