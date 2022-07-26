package com.example.spork.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spork.R;
import com.example.spork.feed.Post;
import com.example.spork.feed.PostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFeedTabFragment extends Fragment {

    private static final String TAG = "ProfileFeedTabFragment";
    private RecyclerView rvUserPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_feed_tab_fragment, container, false);
        rvUserPosts = root.findViewById(R.id.rvUserPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        // set the adapter on the recycler view
        rvUserPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvUserPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            queryPosts();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return root;

    }

    protected void queryPosts() throws ParseException {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER)
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .addDescendingOrder("createdAt")
                .findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> posts, ParseException e) {
                        // check for errors
                        if (e != null) {
                            Log.e(TAG, "Issue with getting posts", e);
                            return;
                        }

                        // save received posts to list and notify adapter of new data
                        allPosts.addAll(posts);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
