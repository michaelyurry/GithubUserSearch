package com.yurry.githubusersearch.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubRestClient {
    private static final String BASE_URL = "https://api.github.com/";
    private static final String PARAM_AUTH_BEARER = "Bearer";

    //Token didapat melalui personal token github saya
    private static final String TOKEN = "49b1254d4f1a19d6914121907583fcaff0f6987f";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static String createAuthBearer() {
        return PARAM_AUTH_BEARER + " " + TOKEN;
    }
}
