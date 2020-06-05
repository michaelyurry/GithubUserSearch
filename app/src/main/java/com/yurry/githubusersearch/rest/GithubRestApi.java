package com.yurry.githubusersearch.rest;

import com.yurry.githubusersearch.model.GithubUserSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GithubRestApi {
    @Headers("Content-Type: application/json")
    @GET("search/users")
    Call<GithubUserSearchResponse> getUser(
            @Header("Authorization") String authorization,
            @Query("q") String username,
            @Query("page") int page,
            @Query("per_page") int perPage);
}
