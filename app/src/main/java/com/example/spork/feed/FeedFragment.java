package com.example.spork.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spork.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment";
    private static final int QUERY_LIMIT = 20;

    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();
        setUpRefreshContainer(view);
        infiniteScroll();
    }

    private void infiniteScroll() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextPosts();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);
    }


    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER)
                .setLimit(QUERY_LIMIT)
                .addDescendingOrder("createdAt")
                .whereLessThan("createdAt", allPosts.get(allPosts.size() - 1).getCreatedAt())
                .findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> posts, ParseException e) {
                        // check for errors
                        if (e != null) {
                            Log.e(TAG, "Issue with getting posts", e);
                            return;
                        }

                        // for debugging purposes let's print every post description to logcat
                        for (Post post : posts) {
                            Log.i(TAG, "Post: by username: " + post.getUser().getUsername());
                        }

                        // save received posts to list and notify adapter of new data
                        allPosts.addAll(posts);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setUpRefreshContainer(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                adapter.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER)
                .setLimit(QUERY_LIMIT)
                .addDescendingOrder("createdAt")
                .findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> posts, ParseException e) {
                        // check for errors
                        if (e != null) {
                            Log.e(TAG, "Issue with getting posts", e);
                            return;
                        }

                        // for debugging purposes let's print every post username to logcat
                        for (Post post : posts) {
                            Log.i(TAG, "Post by username: " + post.getUser().getUsername());
                        }

                        // save received posts to list and notify adapter of new data
                        allPosts.addAll(posts);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}