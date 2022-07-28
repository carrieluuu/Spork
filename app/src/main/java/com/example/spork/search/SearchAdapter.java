package com.example.spork.search;

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
import com.example.spork.Restaurant;
import com.example.spork.restaurant.FetchYelpData;
import com.google.android.material.chip.Chip;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    public static final String TAG = "SearchAdapter";

    private WeakReference<Context> context;
    private List<Restaurant> restaurants;

    public SearchAdapter(Context context, List<Restaurant> restaurants) {
        this.context = new WeakReference<Context>(context);
        this.restaurants = restaurants;
    }

    // Clean all elements of the recycler
    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Restaurant> list) {
        restaurants.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.get()).inflate(R.layout.item_search_restaurant, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivRestaurantImage;
        private TextView tvRestaurantName;
        private TextView tvPriceLevel;
        private Chip chipFeature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvPriceLevel = itemView.findViewById(R.id.tvPriceLevel);
            chipFeature = itemView.findViewById(R.id.chipFeature);
            itemView.setOnClickListener(this);

        }

        public void bind(Restaurant restaurant) {
            // Bind the restaurant data to the view elements
            Glide.with(context.get())
                    .load(restaurant.getImage())
                    .placeholder(R.drawable.restaurant_placeholder)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .transform(new RoundedCorners(100)))
                    .into(ivRestaurantImage);

            tvRestaurantName.setText(restaurant.getName());
            tvPriceLevel.setText(restaurant.getYelpPrice());
            chipFeature.setText(restaurant.getFeature());

        }

        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Restaurant restaurant = restaurants.get(position);

                Object[] yelpData = new Object[2];
                yelpData[0] = null;
                yelpData[1] = restaurant.getYelpId();

                FetchYelpData fetchYelpData  = new FetchYelpData(context.get());
                fetchYelpData.execute(yelpData);
            }
        }

    }

}
