package com.edvaldotsi.nodejsandandroid.retrofit;

import com.edvaldotsi.nodejsandandroid.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Edvaldo on 21/04/2016.
 */
public interface UserService {

    @GET("/api/users/{id}")
    Call<User> getUser(
            @Path("id")
            String id
    );

    @GET("/api/users/me")
    Call<User> getMe();
}
