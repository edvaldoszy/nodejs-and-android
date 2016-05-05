package com.edvaldotsi.nodejsandandroid;

import android.content.Context;
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

    private static Retrofit retrofit;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.api_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        preferences = getSharedPreferences("NodejsAndAndroid", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
    }

    protected <T> T createService(Class<T> service) {
        return retrofit.create(service);
    }
}
