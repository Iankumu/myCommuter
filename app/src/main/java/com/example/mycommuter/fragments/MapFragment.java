package com.example.mycommuter.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mycommuter.R;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.model.LocationModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
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
import com.mapbox.mapboxsdk.style.layers.Property;
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
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.mapbox.core.constants.Constants.PRECISION_6;

import com.example.mycommuter.sharedPrefs.saveSharedPref;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private ImageView hoveringMarker;
    private Button selectLocationButton;
    private FloatingActionButton btnSearchLocation, btnMarker;
    private Layer droppedMarkerLayer;

    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 60000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";

    private static final String LOG_TAG_Code ="Hashcode";
    public static String  Base_URL="http://65c5fc3b72e6.ngrok.io/";
    public static String  URL = Base_URL+"api/location";
    public static String Navigation_url = Base_URL+"api/navigation";
    private FeatureCollection dashedLineDirectionsFeatureCollection;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String DIRECTIONS_LAYER_ID = "DIRECTIONS_LAYER_ID";
    private static final String LAYER_BELOW_ID = "road-label-small";



    private MapFragmentLocationCallback callback = new MapFragmentLocationCallback(this);




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getContext().getApplicationContext(), getString(R.string.map_access_token));

        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        btnSearchLocation = view.findViewById(R.id.btnSearchLocation);
        btnMarker = view.findViewById(R.id.btnShowMarker);
        selectLocationButton = view.findViewById(R.id.select_location_button);

        final boolean[] markerIsShown = {false};

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                MapFragment.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/brayo333/ck9i2acpk1lgg1iqwhfr15mu7"), new Style.OnStyleLoaded() {
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

//                                               postDestinationRequest(destinationLatitude,destinationLongitude);
                                                initDottedLineSourceAndLayer(style);
                                                getRequest();

                                            } else {
                                                selectLocationButton.setBackgroundColor(
                                                        ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));
                                                selectLocationButton.setText("Select Location");

                                                hoveringMarker.setVisibility(View.VISIBLE);

                                                droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                                                if (droppedMarkerLayer != null) {
                                                    droppedMarkerLayer.setProperties(visibility(Property.NONE));
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


                    }
                });
            }
        });


        return view;
    }

    private void postDestinationRequest(String destinationLatitude, String destinationLongitude) {

            String email = saveSharedPref.getEmail(getContext());
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest objectRequest = new StringRequest(
                    Request.Method.POST,
                    Navigation_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                            getRequest();

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
                    params.put("email",email);
                    return params;

                }
                @Override
                public Map<String,String>getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String ,String>();
                    params.put("Application-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            requestQueue.add(objectRequest);


        }

    private void getRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

       JsonObjectRequest objectRequest = new JsonObjectRequest(
               Request.Method.GET,
               Navigation_url,
               null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           JSONArray routes = response.getJSONArray("routes");
                           JSONObject firstroute = routes.getJSONObject(0);
                           String geometry = firstroute.getString("geometry");
                           Log.wtf(LOG_TAG_Code, String.valueOf(geometry));


                           drawNavigationPolylineRoute(firstroute);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }
       );

       requestQueue.add(objectRequest);



    }

    private void drawNavigationPolylineRoute(final JSONObject route) {
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
    }

    private void initDottedLineSourceAndLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID));
        loadedMapStyle.addLayerBelow(
                new LineLayer(
                        DIRECTIONS_LAYER_ID, SOURCE_ID).withProperties(
                        lineWidth(4.5f),
                        lineColor(Color.RED),
                        lineTranslate(new Float[] {0f, 4f}),
                        lineDasharray(new Float[] {1.2f, 1.2f})
                ), LAYER_BELOW_ID);
    }


    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(
                getResources(), R.drawable.blue_marker));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(Property.NONE),
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
                    postCurrentRequest(locationModel.getLatitude(), locationModel.getLongitude());
                } catch (NullPointerException e){

                }

                if (mapFragment.mapboxMap != null && result.getLastLocation() != null) {
                    mapFragment.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        public  void postCurrentRequest(final String latitude, final String longitude){

            String email = saveSharedPref.getEmail(getContext());
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
                    params.put("email", email);
                    return params;
                }
                @Override
                public Map<String,String>getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String ,String>();
                    params.put("Application-Type","application/x-www-form-urlencoded");
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

