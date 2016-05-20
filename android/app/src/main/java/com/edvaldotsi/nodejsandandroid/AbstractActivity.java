package com.edvaldotsi.nodejsandandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edvaldotsi.nodejsandandroid.model.User;
import com.edvaldotsi.nodejsandandroid.retrofit.ServiceGenerator;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Edvaldo on 23/04/2016.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    /* Logged user */
    public static User LOGGED_USER;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get access token from shared preferences if it exists */
        preferences = getSharedPreferences("NodejsAndAndroid", Context.MODE_PRIVATE);
        ServiceGenerator.token = preferences.getString("token", "");
    }
}
