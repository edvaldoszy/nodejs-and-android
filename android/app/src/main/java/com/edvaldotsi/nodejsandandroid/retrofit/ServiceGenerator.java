package com.edvaldotsi.nodejsandandroid.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Edvaldo on 19/05/2016.
 */
public class ServiceGenerator {

    public static String token;

    /* Configure Retrofit */
    private static final String API_BASE_URL = "http://192.168.25.59/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    /* Generate services to Retrofit */
    public static <S> S createService(Class<S> service) {

        /* If access token isn't null ou empty, send it to request header */
        if (!"".equals(token)) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request header
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("x-access-token", token)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(service);
    }
}
