package com.yurry.githubusersearch.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yurry.githubusersearch.R;
import com.yurry.githubusersearch.model.GithubUser;
import com.yurry.githubusersearch.presenter.GithubUserPresenter;
import com.yurry.githubusersearch.presenter.GithubUserPresenterImpl;
import com.yurry.githubusersearch.view.adapter.EndlessScrollListener;
import com.yurry.githubusersearch.view.adapter.GithubUserRVAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GithubUserView {

    private RecyclerView recyclerView;
    private GithubUserRVAdapter adapter;
    private SearchView searchView;
    private RelativeLayout loadingView;
    private GithubUserPresenter presenter;
    private String queryHint, emptyMsg, failedMsg, lastItemMsg;

    private EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new GithubUserPresenterImpl(this);
        initResource();
        initLayout();
        setSearchView();
        setRecyclerView();
    }

    private void initResource(){
        emptyMsg = getResources().getString(R.string.response_empty);
        failedMsg = getResources().getString(R.string.response_failure);
        queryHint = getResources().getString(R.string.query_hint);
        lastItemMsg = getResources().getString(R.string.response_last_item);
    }

    private void initLayout(){
        searchView = findViewById(R.id.github_user_search_bar);
        recyclerView = findViewById(R.id.recycler_view);
        loadingView = findViewById(R.id.loading_layout);
    }

    private void setSearchView(){
        searchView.setQueryHint(queryHint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadingView.setVisibility(View.VISIBLE);
                presenter.onSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //do nothing
                return false;
            }
        });
    }

    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new GithubUserRVAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount >= GithubUserPresenterImpl.PER_PAGE){
                    loadingView.setVisibility(View.VISIBLE);
                    presenter.onSearchNextPage(page);
                } else {
                    makeLastItemToast();
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void makeEmptyToast() {
        Toast.makeText(this, emptyMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeFailedToast() {
        Toast.makeText(this, failedMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeLastItemToast() {
        Toast.makeText(this, lastItemMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setGithubUserList(List<GithubUser> githubUserList) {
        adapter.setGithubUserList(githubUserList);
        scrollListener.resetState();
    }

    @Override
    public void addGithubUserList(List<GithubUser> githubUserList) {
        adapter.addGithubUserList(githubUserList);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }
}
