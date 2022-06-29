package com.example.spork.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.R;
import com.parse.ParseFile;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public static final String TAG = "PostsAdapter";

    private WeakReference<Context> context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = new WeakReference<Context>(context);
        this.posts = posts;
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.get()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private ImageView ivProfilePic;
        private TextView tvLocation;
        private TextView tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTimestamp = itemView.findViewById(R.id.tvTimeStamp);

        }

        public void bind(Post post) {
            int radius = 100;

            // Bind the post data to the view elements
            tvUsername.setText(post.getUser().getUsername());
            tvLocation.setText(post.getLocation().toString());
            Date createdAt = post.getCreatedAt();
            tvTimestamp.setText(Post.calculateTimeAgo(createdAt));

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context.get())
                        .load(image.getUrl())
                        .apply(new RequestOptions()
                                .centerCrop()
                                .transform(new RoundedCorners(radius)))
                        .into(ivImage);
            }

            ParseFile profilePic = post.getUser().getParseFile("profilePic");
            if (profilePic != null) {
                Glide.with(context.get())
                        .load(profilePic.getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfilePic);
            }
        }

    }

}
