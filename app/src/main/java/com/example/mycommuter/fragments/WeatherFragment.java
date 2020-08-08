package com.example.mycommuter.fragments;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.lang.UCharacter;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.R;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.WeatherSearchActivity;
import com.example.mycommuter.adapter.RecyclerItemClickListener;

import com.example.mycommuter.adapter.WeatherAdapter;
import com.example.mycommuter.databinding.FragmentWeatherBinding;
import com.example.mycommuter.factory.ForecastViewHolderFactory;

import com.example.mycommuter.interfaces.CurrentWeather;
import com.example.mycommuter.interfaces.WeatherResultCallback;
import com.example.mycommuter.model.Weather;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.example.mycommuter.utils.IconProvider;
import com.example.mycommuter.viewmodels.ForecastActivityViewModel;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private static final String TAG = "TODOFRAGMENT";
    private WeatherAdapter weatherAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Weather weather;
    private RecyclerView recyclerView;
    private List<Weather> listinit = new ArrayList<>();
    private LinearLayout linearLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ForecastActivityViewModel forecastActivityViewModel;

    Toolbar toolbar;
    FragmentWeatherBinding fragmentWeatherBinding;
    private FusedLocationProviderClient fusedLocationClient;

    public WeatherFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWeatherBinding = fragmentWeatherBinding.inflate(inflater, container, false);
        View view = fragmentWeatherBinding.getRoot();
        linearLayout = view.findViewById(R.id.linearcard);
        recyclerView = view.findViewById(R.id.myweatherRe);
        shimmerFrameLayout = view.findViewById(R.id.shimmerframelay);
        toolbar = view.findViewById(R.id.wthtoolbar);

        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        setShimmer(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView(listinit);


        forecastActivityViewModel = new ViewModelProvider(this).get(ForecastActivityViewModel.class);
        forecastActivityViewModel.init();
        forecastActivityViewModel.getCurrentWeather().observe(getViewLifecycleOwner(), new Observer<Pair<List, Weather>>() {
            @Override
            public void onChanged(Pair<List, Weather> listWeatherPair) {
                setShimmer(false);
                Weather weather1 = listWeatherPair.second;
                forecastActivityViewModel.city.setValue(weather1.getCity());
                forecastActivityViewModel.description.setValue(weather1.getDescription());
                forecastActivityViewModel.temp.setValue(weather1.getTemp() + "°");
                forecastActivityViewModel.weathericon.setValue(weather1.getMain());
                Log.e(TAG, "onChanged: " + weather1.getMain());
                listinit = listWeatherPair.first;
                initRecyclerView(listinit);
                weatherAdapter.notifyDataSetChanged();
            }
        });


        fragmentWeatherBinding.setWeathermodel(forecastActivityViewModel);
        fragmentWeatherBinding.setLifecycleOwner(getViewLifecycleOwner());


    }
    public void setShimmer(boolean shimmer) {
        if (shimmer) {
            shimmerFrameLayout.startShimmer();
        } else {

            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }

    }
    public void getdeviceLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Logic to handle location object
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void initRecyclerView(List<Weather> weather) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        weatherAdapter = new WeatherAdapter(getContext(), weather);

        recyclerView.setAdapter(weatherAdapter);

    }

    @Override
    public void onDetach() {
        listinit.clear();
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        listinit.clear();
        super.onAttach(context);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search1:
                Log.e(TAG, "onOptionsItemSelected: clicked");
                search(item);
                break;
            case R.id.logout_frag:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }




    private void search(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setQueryHint("search ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 3) {


                    Intent intent = new Intent(getContext(), WeatherSearchActivity.class);
                    intent.putExtra("location", query);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        item.getIcon().setVisible(false, false);

    }




    private void logout() {
        saveSharedPref.setLoggedIn(getContext(), new Pair<>(false, ""));
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


