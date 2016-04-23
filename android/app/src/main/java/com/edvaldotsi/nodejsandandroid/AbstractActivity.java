package com.edvaldotsi.nodejsandandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edvaldotsi.nodejsandandroid.model.User;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Edvaldo on 23/04/2016.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    /* Logged user */
    public static User LOGGED_USER;
    protected String token;

    protected Retrofit retrofit;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.25.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        preferences = getSharedPreferences("NodejsAndAndroid", 0);
        token = preferences.getString("token", "");
    }

    protected <T> T getRetrofitService(Class<T> service) {
        return retrofit.create(service);
    }
}
