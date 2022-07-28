package com.example.spork.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spork.R;
import com.example.spork.Restaurant;
import com.example.spork.search.SearchAdapter;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class SavedRestaurantsTabFragment extends Fragment {

    private SearchAdapter adapter;
    private List<Restaurant> savedRestaurants;
    private RecyclerView rvSaved;
    private List<String> savedRestaurantIds;
    private CircularProgressIndicator loadingCircle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.saved_restaurants_tab_fragment, container, false);

        rvSaved = root.findViewById(R.id.rvSaved);
        loadingCircle = root.findViewById(R.id.loading_circle);

        savedRestaurantIds = (ArrayList<String>)ParseUser.getCurrentUser().get("savedRestaurants");

        savedRestaurants = new ArrayList<>();

        if (savedRestaurantIds != null && savedRestaurantIds.size() > 0) {
            Object[] searchByIdData = new Object[5];
            searchByIdData[0] = savedRestaurants;
            searchByIdData[1] = adapter;
            searchByIdData[2] = rvSaved;
            searchByIdData[3] = savedRestaurantIds;
            searchByIdData[4] = loadingCircle;

            YelpSearchById searchById = new YelpSearchById(getContext());
            searchById.execute(searchByIdData);
        }

        return root;
    }


}
