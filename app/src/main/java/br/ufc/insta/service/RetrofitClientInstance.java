package br.ufc.insta.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static Retrofit retrofitLikes;
    private static final String BASE_URL = "http://lbredesocial-1842965994.us-east-1.elb.amazonaws.com:8080";
    private static final String BASE_URL_LIKES = "http://lbredesocial-1842965994.us-east-1.elb.amazonaws.com:8102";

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
