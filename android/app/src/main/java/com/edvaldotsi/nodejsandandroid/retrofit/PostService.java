package com.edvaldotsi.nodejsandandroid.retrofit;

import com.edvaldotsi.nodejsandandroid.model.Post;
import com.edvaldotsi.nodejsandandroid.retrofit.JSON;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Edvaldo on 23/04/2016.
 */
public interface PostService {

    @GET("/api/users/{user_id}/posts")
    Call<JSON<Post>> getUserPosts(
            @Path("user_id")
            long userID,

            @Header("token")
            String token
    );

    @POST("/api/users/{user_id}/posts")
    Call<Void> newPost(
            @Path("user_id")
            long userID,

            @Body
            Post post,

            @Header("token")
            String token
    );
}
