package com.example.spork.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.R;
import com.example.spork.feed.Post;
import com.example.spork.feed.PostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private ImageView ivProfilePic;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvNumPosts;
    private TextView tvNumFriends;
    private TextView tvBio;
    private RecyclerView rvPosts;
    private int numPosts;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvNumPosts = view.findViewById(R.id.tvNumPosts);
        tvNumFriends = view.findViewById(R.id.tvNumFriends);
        tvBio = view.findViewById(R.id.tvBio);
        rvPosts = view.findViewById(R.id.rvUserPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseFile profilePic = currentUser.getParseFile("profilePic");
        if (profilePic != null) {
            Glide.with(getContext())
                    .load(profilePic.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfilePic);
        }

        tvFullName.setText(currentUser.getString("fullName"));
        tvUsername.setText("(@" + currentUser.getUsername() + ")");
        tvBio.setText(currentUser.getString("bio"));

        Log.i(TAG, "bio: " + ParseUser.getCurrentUser().getString("bio"));

        try {
            queryPosts();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvNumPosts.setText(String.valueOf(numPosts));
        tvNumFriends.setText(String.valueOf(currentUser.getInt("numFriends")));

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
        numPosts = query.count();
    }
}