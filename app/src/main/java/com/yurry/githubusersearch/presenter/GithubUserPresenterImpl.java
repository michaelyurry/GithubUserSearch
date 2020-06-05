package com.yurry.githubusersearch.presenter;

import com.yurry.githubusersearch.model.GithubUserSearchResponse;
import com.yurry.githubusersearch.rest.GithubRestApi;
import com.yurry.githubusersearch.rest.GithubRestClient;
import com.yurry.githubusersearch.view.GithubUserView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubUserPresenterImpl implements GithubUserPresenter {
    public static int PER_PAGE = 30;

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
                }
            }

            @Override
            public void onFailure(Call<GithubUserSearchResponse> call, Throwable t) {
                githubUserView.hideLoading();
                githubUserView.makeFailedToast();
            }
        });
    }
}
