package com.example.spork.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.R;
import com.example.spork.feed.Post;
import com.example.spork.feed.PostsAdapter;
import com.example.spork.login.LoginActivity;
import com.example.spork.login.LoginAdapter;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile Fragment class to display user profile details such as friend count, post count,
 * and all of their existing posts composed on the social feed fragment.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private ImageView ivProfilePic;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvNumPosts;
    private TextView tvNumFriends;
    private TextView tvBio;
    private ImageButton btnLogout;
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

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvNumPosts = view.findViewById(R.id.tvNumPosts);
        tvNumFriends = view.findViewById(R.id.tvNumFriends);
        tvBio = view.findViewById(R.id.tvBio);
        btnLogout = view.findViewById(R.id.btnLogout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_feed_tab));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_saved_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ProfileAdapter profileAdapter = new ProfileAdapter(getParentFragmentManager(), getContext(), tabLayout.getTabCount());
        viewPager.setAdapter(profileAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

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

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        try {
            queryPosts();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvNumPosts.setText(String.valueOf(numPosts));
        tvNumFriends.setText(String.valueOf(currentUser.getInt("numFriends")));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issue with logout", e);
                            return;
                        }
                        goLoginActivity();
                    }
                });
            }
        });

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
    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
        startActivity(i);
    }
}