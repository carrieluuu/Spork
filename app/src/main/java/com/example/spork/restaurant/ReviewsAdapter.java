package com.example.spork.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.R;
import com.parse.ParseFile;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
        public static final String TAG = "PostsAdapter";

        private WeakReference<Context> context;
        private List<Review> review;

    public ReviewsAdapter(Context context, List<Review> review) {
            this.context = new WeakReference<Context>(context);
            this.review = review;
        }

        // Clean all elements of the recycler
        public void clear() {
            review.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<Review> list) {
            review.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return review.size();
        }

        @NonNull
        @Override
        public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context.get()).inflate(R.layout.item_review, parent, false);
            return new ReviewsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Review review = reviews.get(position);
            holder.bind(review);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tvUsername;
            private TextView tvReview;
            private ImageView ivProfilePic;
            private TextView tvTimestamp;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvUsername = itemView.findViewById(R.id.tvUsername);
                tvReview = itemView.findViewById(R.id.tvReview);
                ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
                tvTimestamp = itemView.findViewById(R.id.tvTimeStamp);

            }

            public void bind(Review review) {

                // Bind the review data to the view elements
                tvUsername.setText(review.getUser().getUsername());
                Date createdAt = review.getCreatedAt();
                tvTimestamp.setText(review.calculateTimeAgo(createdAt));

                ParseFile profilePic = review.getUser().getParseFile("profilePic");
                if (profilePic != null) {
                    Glide.with(context.get())
                            .load(profilePic.getUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivProfilePic);
                }
            }

        }
}
