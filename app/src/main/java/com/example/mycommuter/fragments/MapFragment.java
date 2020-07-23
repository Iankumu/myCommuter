package com.example.mycommuter.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mycommuter.R;
import com.example.mycommuter.model.LocationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Looper.getMainLooper;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private ImageView hoveringMarker;
    private Button selectLocationButton, button;
    private FloatingActionButton btnSearchLocation, btnMarker, btnStyle;
    private Layer droppedMarkerLayer;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 60000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";

    private static final String LOG_TAG_Code ="Hashcode";
    public static String  Base_URL="http://9650fc7df277.ngrok.io/";
    public static String  URL = Base_URL+"api/location";
    public static String Navigation_url = Base_URL+"api/navigation";
    public static String coordinates = Base_URL+"api/coordinates";
    private FeatureCollection dashedLineDirectionsFeatureCollection;

    //for route 1
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String DIRECTIONS_LAYER_ID = "DIRECTIONS_LAYER_ID";
    private static final String LAYER_BELOW_ID = "road-label-small";

    //for route 2
    private static final String SOURCE_ID2 = "SOURCE_ID2";
    private static final String DIRECTIONS_LAYER_ID2 = "DIRECTIONS_LAYER_ID2";
    private static final String LAYER_BELOW_ID2 = "road-label-small2";

    private MapFragmentLocationCallback callback = new MapFragmentLocationCallback(this);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getContext().getApplicationContext(), getString(R.string.map_access_token));

        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        btnSearchLocation = view.findViewById(R.id.btnSearchLocation);
        btnMarker = view.findViewById(R.id.btnShowMarker);
        btnStyle = view.findViewById(R.id.btnStyleSelector);
        selectLocationButton = view.findViewById(R.id.select_location_button);
        button = view.findViewById(R.id.selectroute);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();
                NavigationLauncher.startNavigation(getActivity(), options);
            }
        });

        final boolean[] markerIsShown = {false};

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                MapFragment.this.mapboxMap = mapboxMap;
//                new Style.Builder().fromUri("mapbox://styles/brayo333/ck9i2acpk1lgg1iqwhfr15mu7")
                String checkMapStyle = saveSharedPref.setMapStyle(getActivity().getApplicationContext());

                String mapStyle;
                if (checkMapStyle!=null){
                    mapStyle = checkMapStyle;
                } else {
                    mapStyle = Style.SATELLITE;
                }

                mapboxMap.setStyle(mapStyle, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {
                        enableLocationComponent(style);

                        btnMarker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!markerIsShown[0]) {
                                    markerIsShown[0] = true;
                                    selectLocationButton.setVisibility(View.VISIBLE);
                                    hoveringMarker = new ImageView(getActivity().getApplicationContext());
                                    hoveringMarker.setImageResource(R.drawable.red_marker);
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                                    hoveringMarker.setLayoutParams(params);
                                    mapView.addView(hoveringMarker);

                                    initDroppedMarker(style);
                                    initDottedLineSourceAndLayer(style);
                                    initDottedLineSourceAndLayer2(style);

                                    selectLocationButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (hoveringMarker.getVisibility() == View.VISIBLE) {
                                                final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

                                                hoveringMarker.setVisibility(View.INVISIBLE);

                                                selectLocationButton.setBackgroundColor(
                                                        ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));
                                                selectLocationButton.setText("Cancel");

                                                if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                                    GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                                                    if (source != null) {
                                                        source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                                                    }
                                                    droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                                                    if (droppedMarkerLayer != null) {
                                                        droppedMarkerLayer.setProperties(visibility(VISIBLE));
                                                    }
                                                }
                                                String destinationLatitude = String.valueOf(mapTargetLatLng.getLatitude());
                                                String destinationLongitude = String.valueOf(mapTargetLatLng.getLongitude());

                                                Toast.makeText(getActivity().getApplicationContext(), "Destination:\t" +
                                                        destinationLatitude + "\t" + destinationLongitude, Toast.LENGTH_SHORT).show();

                                               postDestinationRequest(destinationLatitude,destinationLongitude,style);

//                                                getRequest(style);


                                            } else {
                                                clearRoutes(style);
                                                selectLocationButton.setBackgroundColor(
                                                        ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));
                                                selectLocationButton.setText("Select Location");

                                                hoveringMarker.setVisibility(View.VISIBLE);

                                                droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                                                if (droppedMarkerLayer != null) {
                                                    droppedMarkerLayer.setProperties(visibility(NONE));
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    markerIsShown[0] = false;
//                                    Fragment frg = new MapFragment();
//                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                    if (Build.VERSION.SDK_INT >= 26) {
//                                        ft.setReorderingAllowed(false);
//                                    }
//                                    ft.detach(frg).attach(frg).commit();
                                }
                            }
                        });

                        btnStyle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popup = new PopupMenu(getActivity(), btnStyle);
                                popup.getMenuInflater().inflate(R.menu.navigation_drawer, popup.getMenu());
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.profile_frag:
                                                new saveSharedPref().storeMapStyle(getActivity().getApplicationContext(), Style.MAPBOX_STREETS);
                                                return true;

                                            case R.id.logout_frag:
                                                new saveSharedPref().storeMapStyle(getActivity().getApplicationContext(), Style.SATELLITE_STREETS);
                                                return true;

                                            default:
                                                return true;
                                        }

                                    }
                                });
                                popup.show();
                            }
                        });

                    }
                });
            }
        });


        return view;
    }

    private void postDestinationRequest(String destinationLatitude, String destinationLongitude,@NonNull Style loadedMapStyle) {

        String token = saveSharedPref.getToken(getActivity().getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                Navigation_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            getRequest(loadedMapStyle);
//                            getCoordinatesRequest();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            //Overriding methods to pass data to the server
            @Override
            protected Map<String,String> getParams(){
                //Creating a Map with key and value and send the data to the database
                Map<String,String> params = new HashMap<String ,String>();
                params.put("destinationLatitude", destinationLatitude);
                params.put("destinationLongitude", destinationLongitude);
                return params;

            }
            @Override
            public Map<String,String>getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String ,String>();
                params.put("Application-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " +token);
                return params;
            }
        };

        requestQueue.add(objectRequest);
    }

    private void getRequest(@NonNull Style loadedMapStyle) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String token = saveSharedPref.getToken(getActivity().getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Navigation_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray routes = response.getJSONArray("routes");
                            Log.wtf(LOG_TAG_Code + "\tNo. of routes", String.valueOf(routes.length()));

                            if (routes.length() == 2){
                                JSONObject secondroute = routes.getJSONObject(1);

                                String geometry2 = secondroute.getString("geometry");
                                Log.wtf(LOG_TAG_Code, String.valueOf(geometry2));

                                drawNavigationPolylineRoute2(secondroute, loadedMapStyle);

                            }

                            JSONObject firstroute = routes.getJSONObject(0);


                            String geometry = firstroute.getString("geometry");
                            Log.wtf(LOG_TAG_Code, String.valueOf(geometry));



                            drawNavigationPolylineRoute(firstroute, loadedMapStyle);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // do stuff here
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String> ();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(objectRequest);

    }
//    private void getCoordinatesRequest() {
//        String token = saveSharedPref.getToken(getActivity().getApplicationContext());
//        final LocationModel locationModel = new LocationModel();
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        JsonObjectRequest objectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                coordinates,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jo = response.getJSONArray("data");
//                            String curlat = (String) jo.get(0);
//                            String curlong = (String) jo.get(1);
//                            String destlat = (String) jo.get(2);
//                            String destlong = (String) jo.get(3);
//
//                            locationModel.setCurrentLatitude(curlat);
//                            locationModel.setCurrentLongitude(curlong);
//                            locationModel.setDestinationLatitude(destlat);
//                            locationModel.setDestinationLongitude(destlong);
//                            Log.d("Cooridinates", "CurrentLatitude:"+String.valueOf(curlat));
//                            Log.d("Cooridinates", "CurrentLongitude:"+String.valueOf(curlong));
//                            Log.d("Cooridinates", "DestinationLatitude:"+String.valueOf(destlat));
//                            Log.d("Cooridinates","DestinationLongitude:"+ String.valueOf(destlong));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // do stuff here
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String> ();
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//
//        requestQueue.add(objectRequest);
//
//
//    }


    private void drawNavigationPolylineRoute(final JSONObject route, @NonNull Style loadedMapStyle) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    List<Feature> directionsRouteFeatureList = new ArrayList<>();
                    try {
                        String geometry = route.getString("geometry");
                        LineString lineString = LineString.fromJson(geometry);
//                        LineString lineString = LineString.fromPolyline(geometry, PRECISION_6);
                        List<Point> coordinates = lineString.coordinates();
                        for (int i = 0; i < coordinates.size(); i++) {
                            directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(coordinates)));
                        }
                        dashedLineDirectionsFeatureCollection = FeatureCollection.fromFeatures(directionsRouteFeatureList);
                        GeoJsonSource source = style.getSourceAs(SOURCE_ID);
                        if (source != null) {
                            source.setGeoJson(dashedLineDirectionsFeatureCollection);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Layer route1 = loadedMapStyle.getLayer(DIRECTIONS_LAYER_ID);
        assert route1 != null;

        if (NONE.equals(route1.getVisibility().getValue())) {
            route1.setProperties(visibility(VISIBLE));
        }

        Point destinationPoint = Point.fromLngLat(36.843069,-1.306865);
        Point originPoint = Point.fromLngLat(36.8385369, -1.3128765);

        NavigationRoute.builder(getActivity().getApplicationContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, retrofit2.Response<DirectionsResponse> response) {
                        Log.d("Ha", "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e("Ha", "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e("Ha", "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map (Only draws one route) refer to comment on: https://github.com/mapbox/mapbox-navigation-android/issues/467
//                        if (navigationMapRoute != null) {
//                            navigationMapRoute.removeRoute();
//                        } else {
//                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
//                        }
//                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }

    private void drawNavigationPolylineRoute2(final JSONObject route, @NonNull Style loadedMapStyle) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    List<Feature> directionsRouteFeatureList = new ArrayList<>();
                    try {
                        String geometry = route.getString("geometry");
                        LineString lineString = LineString.fromJson(geometry);
//                        LineString lineString = LineString.fromPolyline(geometry, PRECISION_6);
                        List<Point> coordinates = lineString.coordinates();
                        for (int i = 0; i < coordinates.size(); i++) {
                            directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(coordinates)));
                        }
                        dashedLineDirectionsFeatureCollection = FeatureCollection.fromFeatures(directionsRouteFeatureList);
                        GeoJsonSource source = style.getSourceAs(SOURCE_ID2);
                        if (source != null) {
                            source.setGeoJson(dashedLineDirectionsFeatureCollection);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Layer route2 = loadedMapStyle.getLayer(DIRECTIONS_LAYER_ID2);
        assert route2 != null;

        if (NONE.equals(route2.getVisibility().getValue())) {
            route2.setProperties(visibility(VISIBLE));
        }
    }

    private void initDottedLineSourceAndLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID));
        loadedMapStyle.addLayerBelow(
                new LineLayer(
                        DIRECTIONS_LAYER_ID, SOURCE_ID).withProperties(
                        lineWidth(4.5f),
                        lineColor(Color.GREEN),
                        lineTranslate(new Float[] {0f, 4f}),
                        lineDasharray(new Float[] {1.2f, 1.2f})
                ), LAYER_BELOW_ID);
    }

    private void initDottedLineSourceAndLayer2(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID2));
        loadedMapStyle.addLayerBelow(
                new LineLayer(
                        DIRECTIONS_LAYER_ID2, SOURCE_ID2).withProperties(
                        lineWidth(4.5f),
                        lineColor(Color.RED),
                        lineTranslate(new Float[] {0f, 4f}),
                        lineDasharray(new Float[] {1.2f, 1.2f})
                ), LAYER_BELOW_ID2);
    }

    private void clearRoutes(@NonNull Style loadedMapStyle){
        Layer route1 = loadedMapStyle.getLayer(DIRECTIONS_LAYER_ID);
        Layer route2 = loadedMapStyle.getLayer(DIRECTIONS_LAYER_ID2);

        assert route1 != null;
        route1.setProperties(visibility(NONE));
        assert route2 != null;
        route2.setProperties(visibility(NONE));
    }


    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(
                getResources(), R.drawable.blue_marker));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)

        ));
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getActivity().getApplicationContext())) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity().getApplicationContext(), loadedMapStyle).build());

            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(getActivity().getApplicationContext());

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    private  class MapFragmentLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapFragment> mapFragmentWeakReference;

        MapFragmentLocationCallback(MapFragment mapFragment) {
            this.mapFragmentWeakReference = new WeakReference<>(mapFragment);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapFragment mapFragment = mapFragmentWeakReference.get();

            if (mapFragment != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                Double latitude =result.getLastLocation().getLatitude();
                Double longitude=result.getLastLocation().getLongitude();

                final LocationModel locationModel = new LocationModel();

                locationModel.setLatitude(String.valueOf(latitude));
                locationModel.setLongitude(String.valueOf(longitude));

                try {
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            locationModel.getLatitude() + "\t" +  locationModel.getLongitude(), Toast.LENGTH_SHORT).show();
//
//
////                    String userToken = prefs.getToken(getActivity().getApplicationContext());
//                    String email = saveSharedPref.getEmail(getContext());
//                    Log.d("mapemail",email);
//
////                    Toast.makeText(getActivity().getApplicationContext(), userToken, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity().getApplicationContext(), email, Toast.LENGTH_SHORT).show();
//                    postCurrentRequest(locationModel.getLatitude(), locationModel.getLongitude());
                } catch (NullPointerException e){

                }

                if (mapFragment.mapboxMap != null && result.getLastLocation() != null) {
                    mapFragment.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        public  void postCurrentRequest(final String latitude, final String longitude){

            String token = saveSharedPref.getToken(getContext());
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest objectRequest = new StringRequest(
                    Request.Method.POST,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                //Overriding methods to pass data to the server
                @Override
                protected Map<String,String> getParams(){
                    //Creating a Map with key and value and send the data to the database
                    Map<String,String> params = new HashMap<String ,String>();
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    return params;
                }
                @Override
                public Map<String,String>getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String ,String>();
                    params.put("Application-Type","application/x-www-form-urlencoded");
                    params.put("Authorization", "Bearer " +token);
                    return params;
                }
            };

            requestQueue.add(objectRequest);

        }

//        private void getRequest(String latitude, String longitude, String destinationLatitude, String destinationLongitude) {
//            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//
//            JsonObjectRequest objectRequest = new JsonObjectRequest(
//                    Request.Method.GET,
//                    Navigation_url,
//                    null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            if (response != null){
//                                Log.d(LOG_TAG_Code,"The body has DATA");
//                                Log.d(LOG_TAG_Code,String.valueOf(response));
//                                try {
//                                    String routes = response.getString("routes");
//                                    Log.d(LOG_TAG_Code,routes);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }else{
//                                Log.d(LOG_TAG_Code,"The body is Empty");
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            }
//            );
//
//            requestQueue.add(objectRequest);
//
//        }


        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Fragment mapFragment = mapFragmentWeakReference.get();
            if (mapFragment != null) {
                Toast.makeText(getActivity().getApplicationContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity().getApplicationContext(), R.string.permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), R.string.notify_permission_required, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
