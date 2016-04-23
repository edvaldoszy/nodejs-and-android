package com.edvaldotsi.nodejsandandroid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Edvaldo on 22/04/2016.
 */
public class UserAuth {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("message")
    @Expose
    private String message;

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
