package com.yurry.githubusersearch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yurry.githubusersearch.R;
import com.yurry.githubusersearch.model.GithubUser;

import java.util.ArrayList;
import java.util.List;

public class GithubUserRVAdapter extends RecyclerView.Adapter<GithubUserRVAdapter.GithubUserViewHolder> {
    private List<GithubUser> githubUserList = new ArrayList<>();

    @NonNull
    @Override
    public GithubUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.github_user_item, parent, false);
        return new GithubUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubUserViewHolder holder, int position) {
        GithubUser githubUser = getItem(position);
        renderUserImage(holder, githubUser);
        renderUsername(holder, githubUser);
    }

    @Override
    public int getItemCount() {
        return githubUserList.size();
    }

    private GithubUser getItem(int index) {
        return githubUserList.get(index);
    }

    private void renderUserImage(GithubUserViewHolder holder, GithubUser githubUser){
        Picasso.get().load(githubUser.getAvatarUrl()).into(holder.imageView);
    }

    private void renderUsername(GithubUserViewHolder holder, GithubUser githubUser){
        holder.userNameText.setText(githubUser.getLogin());
    }

    public void setGithubUserList(List<GithubUser> githubUsers) {
        githubUserList.clear();
        githubUserList.addAll(githubUsers);
        notifyDataSetChanged();
    }

    public void addGithubUserList(List<GithubUser> githubUsers) {
        githubUserList.addAll(githubUsers);
        notifyDataSetChanged();
    }

    class GithubUserViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView userNameText;

        GithubUserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.github_user_picture);
            userNameText = itemView.findViewById(R.id.github_user_name);
        }
    }
}
