package br.ufc.insta.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static Retrofit retrofitLikes;
    private static final String BASE_URL = "http://ec2-3-87-206-82.compute-1.amazonaws.com:8080";
    private static final String BASE_URL_LIKES = "http://ec2-3-87-206-82.compute-1.amazonaws.com:8102";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceLikes() {
        if (retrofitLikes == null) {
            retrofitLikes = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_LIKES)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitLikes;
    }
}
