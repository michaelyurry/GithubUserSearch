package com.yurry.githubusersearch.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yurry.githubusersearch.model.GithubUserSearchResponse;
import com.yurry.githubusersearch.rest.GithubRestApi;
import com.yurry.githubusersearch.rest.GithubRestClient;
import com.yurry.githubusersearch.view.GithubUserView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubUserPresenterImpl implements GithubUserPresenter {
    public static int PER_PAGE = 30;
    private static String MESSAGE_KEY = "message";

    private GithubUserView githubUserView;
    private GithubRestApi api;
    private String lastQuery;

    public GithubUserPresenterImpl(GithubUserView userView){
        this.githubUserView = userView;
        api = GithubRestClient.getClient().create(GithubRestApi.class);
    }

    @Override
    public void onSearch(String query) {
        lastQuery = query;
        //start query data in page 1
        Call<GithubUserSearchResponse> call = api.getUser(GithubRestClient.createAuthBearer(), query, 1, PER_PAGE);
        call.enqueue(new Callback<GithubUserSearchResponse>() {
            @Override
            public void onResponse(Call<GithubUserSearchResponse> call, Response<GithubUserSearchResponse> response) {
                if (response.isSuccessful()){
                    githubUserView.hideLoading();
                    if (response.body() != null) {
                        githubUserView.setGithubUserList(response.body().getItems());
                        if(response.body().getTotalCount() == 0){
                            githubUserView.makeEmptyToast();
                        }
                    } else {
                        githubUserView.makeFailedToast();
                    }
                } else {
                    handleResponseError(response);
                }
            }

            @Override
            public void onFailure(Call<GithubUserSearchResponse> call, Throwable t) {
                githubUserView.hideLoading();
                githubUserView.makeFailedToast();
            }
        });
    }

    @Override
    public void onSearchNextPage(int page) {
        Call<GithubUserSearchResponse> call = api.getUser(GithubRestClient.createAuthBearer(), lastQuery, page, PER_PAGE);
        call.enqueue(new Callback<GithubUserSearchResponse>() {
            @Override
            public void onResponse(Call<GithubUserSearchResponse> call, Response<GithubUserSearchResponse> response) {
                if (response.isSuccessful()){
                    githubUserView.hideLoading();
                    if (response.body() != null) {
                        githubUserView.addGithubUserList(response.body().getItems());
                        if (response.body().getItems().size() == 0){
                            githubUserView.makeLastItemToast();
                        }
                    } else {
                        githubUserView.makeFailedToast();
                    }
                } else {
                    handleResponseError(response);

                }
            }

            @Override
            public void onFailure(Call<GithubUserSearchResponse> call, Throwable t) {
                githubUserView.hideLoading();
                githubUserView.makeFailedToast();
            }
        });
    }

    private void handleResponseError(Response<GithubUserSearchResponse> response){
        githubUserView.hideLoading();
        Gson gson = new Gson();
        try {
            if (response.errorBody() != null) {
                JsonObject jsonObject = gson.fromJson(response.errorBody().string(), JsonObject.class);
                if(jsonObject.get(MESSAGE_KEY).isJsonPrimitive() && jsonObject.get(MESSAGE_KEY).getAsJsonPrimitive().isString()){
                    githubUserView.makeToast(jsonObject.get(MESSAGE_KEY).getAsString());
                } else {
                    githubUserView.makeFailedToast();
                }
            } else {
                githubUserView.makeFailedToast();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
