package com.yurry.githubusersearch.presenter;

public interface GithubUserPresenter {
    void onSearch(String query);
    void onSearchNextPage(int page);
}
