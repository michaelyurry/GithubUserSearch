package com.yurry.githubusersearch.view;

import com.yurry.githubusersearch.model.GithubUser;

import java.util.List;

public interface GithubUserView {
    void makeEmptyToast();
    void makeFailedToast();
    void makeLastItemToast();
    void makeToast(String s);
    void setGithubUserList (List<GithubUser> githubUserList);
    void addGithubUserList (List<GithubUser> githubUserList);
    void hideLoading();
}
